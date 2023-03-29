package cn.edu.fc.mapper.po;

import io.lettuce.core.StrAlgoArgs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "preference")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferencePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Byte type;

    private Long staffId;

    private String value;
}
