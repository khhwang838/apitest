package keichee.study.dl4j.domain;

import java.util.List;

public class AccuracyDto {

	private List<String> questions;

	public List<String> getQuestions() {
		return questions;
	}

	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccuracyDto [questions=");
		builder.append(questions);
		builder.append("]");
		return builder.toString();
	}
}
