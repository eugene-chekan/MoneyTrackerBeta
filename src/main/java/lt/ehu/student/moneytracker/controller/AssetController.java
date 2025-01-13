package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.model.Asset;
import lt.ehu.student.moneytracker.service.AssetService;
import lt.ehu.student.moneytracker.service.CurrencyService;
import lt.ehu.student.moneytracker.service.UserService;
import lt.ehu.student.moneytracker.service.AssetTypeService;
import lt.ehu.student.moneytracker.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Controller
@RequestMapping("/assets")
@Secured({"USER", "ADMIN"})
public class AssetController {
    private final AssetService assetService;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final AssetTypeService assetTypeService;

    @Autowired
    public AssetController(AssetService assetService,
                         UserService userService,
                         CurrencyService currencyService,
                         AssetTypeService assetTypeService) {
        this.assetService = assetService;
        this.userService = userService;
        this.currencyService = currencyService;
        this.assetTypeService = assetTypeService;
    }

    @GetMapping
    public String listAssets(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        var assets = assetService.findByUserId(user.getId());
        
        // Calculate total balances by currency
        Map<Currency, BigDecimal> totalBalances = assets.stream()
            .collect(Collectors.groupingBy(
                Asset::getCurrency,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    Asset::getBalance,
                    BigDecimal::add
                )
            ));
            
        model.addAttribute("assets", assets);
        model.addAttribute("totalBalances", totalBalances);
        return "assets/list";
    }

    @GetMapping("/new")
    public String newAssetForm(Model model) {
        model.addAttribute("asset", new Asset());
        model.addAttribute("currencies", currencyService.findAll());
        model.addAttribute("assetTypes", assetTypeService.findAll());
        return "assets/form";
    }

    @PostMapping
    public String createAsset(@ModelAttribute Asset asset, 
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        // Load the full entities based on IDs
        asset.setType(assetTypeService.findById(asset.getType().getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid asset type")));
            
        asset.setCurrency(currencyService.findById(asset.getCurrency().getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid currency")));
            
        asset.setUser(user);
        assetService.save(asset);
        
        redirectAttributes.addFlashAttribute("success", "Asset was successfully created!");
        return "redirect:/assets";
    }

    @GetMapping("/{id}/edit")
    public String editAssetForm(@PathVariable UUID id, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        var asset = assetService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Asset not found"));
            
        model.addAttribute("asset", asset);
        model.addAttribute("currencies", currencyService.findAll());
        model.addAttribute("assetTypes", assetTypeService.findAll());
        return "assets/form";
    }

    @PostMapping("/{id}/delete")
    public String deleteAsset(@PathVariable UUID id,
                            @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        if (!assetService.findByIdAndUserId(id, user.getId()).isPresent()) {
            throw new IllegalStateException("Asset not found");
        }
            
        assetService.delete(id);
        return "redirect:/assets";
    }

    @PostMapping("/{id}/edit")
    public String updateAsset(@PathVariable UUID id,
                             @ModelAttribute Asset asset,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        var existingAsset = assetService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Asset not found"));
        
        // Update the existing asset's properties
        existingAsset.setName(asset.getName());
        existingAsset.setType(assetTypeService.findById(asset.getType().getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid asset type")));
        existingAsset.setCurrency(currencyService.findById(asset.getCurrency().getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid currency")));
        existingAsset.setBalance(asset.getBalance());
        existingAsset.setDescription(asset.getDescription());
        
        assetService.save(existingAsset);
        
        redirectAttributes.addFlashAttribute("success", "Asset was successfully updated!");
        return "redirect:/assets";
    }
}