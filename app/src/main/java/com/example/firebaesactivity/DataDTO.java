package com.example.firebaesactivity;

import com.github.mikephil.charting.data.Entry;
import java.util.List;

public class DataDTO {
    private List<Entry> entries;

    public DataDTO() {}
    public DataDTO(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getentries() {
        return entries ;
    }


}
