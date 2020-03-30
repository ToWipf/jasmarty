package org.wipf.wipfapp.datatypes;

import org.json.JSONObject;

/**
 * @author wipf
 *
 */
public class MumelSpieler extends Game {
	private Integer PlayerId;

	private MumelValue Leben;
	private MumelValue Feuer;
	private MumelValue Blitz;
	private MumelValue Wasser;
	private MumelValue Stein;
	private MumelValue DoppelFeuer;
	private MumelValue DoppelWasser;
	private MumelValue SchwarzerStein;
	private MumelValue Geld;
	private MumelValue HausBrennen;
	private MumelValue BunterStein;

	/**
	 * 
	 */
	public MumelSpieler() {
		this.Leben = new MumelValue();
		this.Feuer = new MumelValue();
		this.Blitz = new MumelValue();
		this.Wasser = new MumelValue();
		this.Stein = new MumelValue();
		this.DoppelFeuer = new MumelValue();
		this.DoppelWasser = new MumelValue();
		this.SchwarzerStein = new MumelValue();
		this.Geld = new MumelValue();
		this.HausBrennen = new MumelValue();
		this.BunterStein = new MumelValue();
	}

	/**
	 * 
	 */
	public void newFill() {
		this.Leben.setVal(5);
		this.Feuer.setVal(1);
		this.Blitz.setVal(2);
		this.Wasser.setVal(2);
		this.Stein.setVal(1);
		this.DoppelFeuer.setVal(0);
		this.DoppelWasser.setVal(0);
		this.SchwarzerStein.setVal(1);
		this.Geld.setVal(10);
		this.HausBrennen.setVal(0);
		this.BunterStein.setVal(0);
	}

	/**
	 * @return
	 */
	public String exportToJson() {
		return new JSONObject().put("leben", Leben).put("feuer", Feuer).put("blitz", Blitz).put("wasser", Wasser)
				.put("stein", Stein).put("doppelfeuer", DoppelFeuer).put("doppelwasser", DoppelWasser)
				.put("schwarzerstein", SchwarzerStein).put("geld", Geld).put("hausbrennen", HausBrennen)
				.put("bunterstein", BunterStein).toString();
	}

	public Integer getLeben() {
		return Leben.getVal();
	}

	public void setLeben(MumelValue leben) {
		Leben = leben;
	}

	public Integer getFeuer() {
		return Feuer.getVal();
	}

	public void setFeuer(MumelValue feuer) {
		Feuer = feuer;
	}

	public Integer getBlitz() {
		return Blitz.getVal();
	}

	public void setBlitz(MumelValue blitz) {
		Blitz = blitz;
	}

	public Integer getWasser() {
		return Wasser.getVal();
	}

	public void setWasser(MumelValue wasser) {
		Wasser = wasser;
	}

	public Integer getStein() {
		return Stein.getVal();
	}

	public void setStein(MumelValue stein) {
		Stein = stein;
	}

	public Integer getDoppelWasser() {
		return DoppelWasser.getVal();
	}

	public void setDoppelWasser(MumelValue doppelWasser) {
		DoppelWasser = doppelWasser;
	}

	public Integer getDoppelFeuer() {
		return DoppelFeuer.getVal();
	}

	public void setDoppelFeuer(MumelValue doppelFeuer) {
		DoppelFeuer = doppelFeuer;
	}

	public Integer getSchwarzerStein() {
		return SchwarzerStein.getVal();
	}

	public void setSchwarzerStein(MumelValue schwarzerStein) {
		SchwarzerStein = schwarzerStein;
	}

	public Integer getGeld() {
		return Geld.getVal();
	}

	public void setGeld(MumelValue geld) {
		Geld = geld;
	}

	public Integer getHausBrennen() {
		return HausBrennen.getVal();
	}

	public void setHausBrennen(MumelValue hausBrennen) {
		HausBrennen = hausBrennen;
	}

	public Integer getBunterStein() {
		return BunterStein.getVal();
	}

	public void setBunterStein(MumelValue bunterStein) {
		BunterStein = bunterStein;
	}

	public Integer getPlayerId() {
		return PlayerId;
	}

	public void setPlayerId(Integer playerId) {
		PlayerId = playerId;
	}

}
