package com.portfolio.ae.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Node nested inside {@link Category}: {@code {"usertype": {"usertype": "Women"}}}. */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usertype {

    private String usertype;
}
