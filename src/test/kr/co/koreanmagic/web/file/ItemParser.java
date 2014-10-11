package kr.co.koreanmagic.web.file;


// 사이즈 보고 품목 알려주기
public class ItemParser {
	
	
	static interface ItemEntry {
		String getName();
		String[] getItems();
	}
	
	
	static class Sticker implements ItemEntry {
		private static final String[] VALUES = {"50-30", "60-50", "210-147", "32-50", "47-64", "90-160", "55-15", "70-110",
												"115-40", "190-95", "75-50", "55-25", "60-35", "60-40", "95-165", "185-90", "150-130"};
		@Override
		public String getName() { return "스티커"; }
		@Override
		public String[] getItems() { return VALUES; }
	}
	
	static class Card implements ItemEntry {
		private static final String[] VALUES = { "92-52", "90-50", "90-58", "88-54", "86-104", "86-54", "88-104", "88-108",
			"88-56", "52-92", "50-90", "108-88", "92-104" };
		@Override
		public String getName() { return "명함"; }
		@Override
		public String[] getItems() { return VALUES; }
	}
	
	static class Print implements ItemEntry {
		private static final String[] VALUES = { "262-187", "299-212", "297-210", "424-299", "297-420", "210-297", "260-186", "299-424",
			"260-187", "186-260", "187-262", "297-422", "262-374", "374-262", "424-598", "212-299"};
		@Override
		public String getName() { return "전단"; }
		@Override
		public String[] getItems() { return VALUES; }
	}

	static class Catalogue implements ItemEntry {
		private static final String[] VALUES = { "185-264", "190-264", "210-260" };
		@Override
		public String getName() { return "카다로그"; }
		@Override
		public String[] getItems() { return VALUES; }
	}
	

	private static final ItemEntry[] items = {new Card(), new Catalogue(), new Sticker(), new Print()};
	
	
	public static String searchItem(String size) {
		if(size == null) return size;
		String result = null;
		for(ItemEntry i : items) {
			if((result = search(i, size)) != null)
				return result;
		}
		return result;
	}
	
	private static String search(ItemEntry entry, String size) {
		String result = null;
		for(String s : entry.getItems()) {
			if(s.equals(size)) {
				return entry.getName(); // 일치하는게 있으면 이름을 돌려준다.
			}
		}
		return result;
	}
	
}
