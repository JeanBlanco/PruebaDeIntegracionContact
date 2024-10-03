package edu.unac.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Contact {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
}
