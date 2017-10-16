package cn.mldn.test;

public class TestSplit {
	public static void main(String[] args) {
		String s = "fdfdfdf|";
		String[] sa = s.split("\\|");
		System.out.println(sa.length);
		System.out.println(sa[0]);
	}

}
