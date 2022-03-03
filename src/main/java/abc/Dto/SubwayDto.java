package abc.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubwayDto {
    private String subwayid;
    private String statnid;
    private String statnname;
}
