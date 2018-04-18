package keichee.study.aibot.apitest.domain;

import java.util.List;

public class ReturnDto {

	private int numFound;
	private List<Doc> docs;
	
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public List<Doc> getDocs() {
		return docs;
	}
	public void setDocs(List<Doc> docs) {
		this.docs = docs;
	}

	public class Doc {
		private String TITLE;
		private String MOLLEH_SVC_URL;
		private String PRICE;
		private String PRICEADD;
		
		public Doc(String title, String svcUrl, String price, String priceAdd) {
			this.TITLE = title;
			this.MOLLEH_SVC_URL = svcUrl;
			this.PRICE = price;
			this.PRICEADD = priceAdd;
		}
		public String getTITLE() {
			return TITLE;
		}
		public void setTITLE(String tITLE) {
			TITLE = tITLE;
		}
		public String getMOLLEH_SVC_URL() {
			return MOLLEH_SVC_URL;
		}
		public void setMOLLEH_SVC_URL(String mOLLEH_SVC_URL) {
			MOLLEH_SVC_URL = mOLLEH_SVC_URL;
		}
		public String getPRICE() {
			return PRICE;
		}
		public void setPRICE(String pRICE) {
			PRICE = pRICE;
		}
		public String getPRICEADD() {
			return PRICEADD;
		}
		public void setPRICEADD(String pRICEADD) {
			PRICEADD = pRICEADD;
		}
	}
}
