package processor.cachesystem;

import java.util.Arrays;
import java.util.List;

public class cacheLine {

    Integer tag;
    Integer data;
    boolean isEmpty;

    cacheLine(){
        this.isEmpty = true;
    }
    
    public Integer getTag() {
        return tag;
    }
    public void setTag(Integer tag) {
        this.tag = tag;
        setEmpty(false);
    }
    public Integer getData() {
        return data;
    }
    public void setData(Integer data) {
        this.data = data;
    }

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
}
