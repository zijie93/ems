package io.openems.edge.goodwe.et;

import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.ess.api.SymmetricEss;
import io.openems.edge.goodwe.et.charger.GoodweETChargerPV1;

public interface GoodweET extends SymmetricEss, OpenemsComponent{
	
	public Integer getUnitId();

	public String getModbusBridgeId();

	public void setCharger(GoodweETChargerPV1 charger);

}
