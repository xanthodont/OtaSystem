package areas.ota.models;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;


@Embeddable
public class DeltaPk implements Serializable {
	private long fromVersionId;
	private long toVersionId;
	
	public DeltaPk() {
		
	}
	public DeltaPk(long fromVersionId, long toVersionId) {
		this.fromVersionId = fromVersionId;
		this.toVersionId = toVersionId;
	}
}
