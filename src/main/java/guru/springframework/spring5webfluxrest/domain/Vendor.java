package guru.springframework.spring5webfluxrest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Vendor {

    @Id
    private String id;

    private String name;

    private String lastName;

    public boolean equals(Vendor other) {
        return this.name.equals(other.name) && this.lastName.equals(other.lastName);
    }

}
