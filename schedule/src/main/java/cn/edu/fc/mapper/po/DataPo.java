package cn.edu.fc.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    private LocalDate date;

    private String beginTime;

    private String endTime;

    private Float num;
}
