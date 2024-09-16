package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CartaoStatusEnum {

    INATIVO( "INATIVO" ),
    ATIVO( "ATIVO" );

    private String value;

    public static CartaoStatusEnum fromValue( String value ) {
        return Arrays.stream( values() )
                .filter( v -> v.getValue().equalsIgnoreCase( value ) )
                .findFirst()
                .orElse( null );
    }

}
