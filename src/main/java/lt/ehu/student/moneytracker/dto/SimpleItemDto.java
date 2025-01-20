package lt.ehu.student.moneytracker.dto;

import lombok.Data;

@Data
public class SimpleItemDto {
    private Object id;  // Use Object to handle both Long and UUID
    private String name;
    private String emojiIcon;

    public SimpleItemDto(Object id, String name) {
        this.id = id;
        this.name = name;
        this.emojiIcon = null;
    }

    public SimpleItemDto(Object id, String name, String emojiIcon) {
        this.id = id;
        this.name = name;
        this.emojiIcon = emojiIcon;
    }
}