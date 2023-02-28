package pl.bykodev.messenger_api.integration_tests.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusDeserialization {
    private String timestamp;
    private String status;
    private String path;
}
