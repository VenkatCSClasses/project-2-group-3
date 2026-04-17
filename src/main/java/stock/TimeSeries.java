package stock;

import java.util.List;

public class TimeSeries {
    private Meta meta;
    private List<Values> values;

    public TimeSeries(Meta meta, List<Values> values) {
        this.meta = meta;
        this.values = values;
    }

    public Meta getMeta() {
        return meta;
    }

    public List<Values> getValues() {
        return values;
    }
}