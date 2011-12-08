package devhood.im.sim.model;

/**
 * 
 * 
 * @author Schiefele.Andreas
 */
public enum Color {
	
	BLACK("#000000"),
	ROT("#BB0000"),
	GRÜN("009900"),
	BLAU("#0000DD"),
	ORANGE("#FF8400"),
	TÜRKIS("#009C8B"),
	LILA("#6C00AA"),
	ROSA("#FF00D8"),
	BRAUN("#A55D00"),
	GRAU("#666666"),
	ROT_PASTELL("#D34144"),
	GRÜN_PASTELL("#589056"),
	BLAU_PASTELL("#4D78F4"),
	ORANGE_PASTELL("#DA9B42"),
	TÜRKIS_PASTELL("#54BBB0"),
	LILA_PASTELL("#925EB0"),
	ROSA_PASTELL("#EB97DE"),
	BRAUN_PASTELL("#C69555");
	
	private String hexValue;
	
	private Color(String hexValue) {
		this.hexValue = hexValue;
	}

	public String getHexValue(){
		return this.hexValue;
	}
}
