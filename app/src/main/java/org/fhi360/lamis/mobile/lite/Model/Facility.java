package org.fhi360.lamis.mobile.lite.Model;

import lombok.Data;

@Data
public class Facility {
    private long facility_id;
    private String name;
    private long state_id;
    private long lga_id;
    private long deviceconfig_id;


}
