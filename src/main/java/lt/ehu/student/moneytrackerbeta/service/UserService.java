package lt.ehu.student.moneytrackerbeta.service;

import java.util.List;

import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.User;

public interface UserService {
    boolean verifyLogin(String username, String password) throws ServiceException;

    boolean registerUser(String username, String password, String firstName, String lastName, int defaultCurrency,
            String email) throws ServiceException;

    boolean isUsernameTaken(String username) throws ServiceException;

    String getFirstName(String username) throws ServiceException;

    int getUserId(String username) throws ServiceException;

    List<Asset> findAssets(int userId) throws ServiceException;

    User findUser(String username) throws ServiceException;
}
