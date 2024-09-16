package api.dto.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {

    @NonNull
    @Schema(name = "Status Code", example = "400", required = true)
    private Integer status;

    @NonNull
    @Schema(name = "Error Code", example = "400001", required = true)
    private String code;

    @NonNull
    @Schema(name = "Error message", example = "Item não encontrado", required = true)
    private String message;

    @Schema(name = "Error details", example = "Não foi possível encontrar o item informado")
    private String details;

}