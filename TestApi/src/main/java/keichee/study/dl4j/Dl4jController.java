package keichee.study.dl4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import keichee.study.dl4j.domain.AccuracyDto;
import keichee.study.dl4j.domain.PosNegDto;

@Controller
@RequestMapping("/dl4j")
public class Dl4jController {

	private final static Logger log = LoggerFactory.getLogger(Dl4jController.class);
	
	private Word2Vec vec;
	
	/**
	 * 학습시키기
	 * @return
	 */
	@RequestMapping(value = "/learn", method = RequestMethod.GET)
	@ResponseBody
	public Object learn(String filePath) {
		
		log.info("Loading data....");
		ClassPathResource resource = new ClassPathResource(filePath);
//		ClassPathResource resource = new ClassPathResource("test_raw_sentences.txt");
//		ClassPathResource resource = new ClassPathResource("kowiki-20180401-pages-articles.xml");
		SentenceIterator iter = null;
		try {
			iter = new LineSentenceIterator(resource.getFile());
			iter.setPreProcessor(new SentencePreProcessor() {
				public String preProcess(String sentence) {
					// TODO : 형태소 분석하여 띄어쓰기 된 명사들로 이뤄진 문장 리턴
					return sentence.toLowerCase();
				}
			});
		} catch (IOException e) {
			log.error("SentenceIterator initialization failed. {}", e);
			return e.getMessage();
		}
		
		/**
		 * 데이터 토큰화 하기
		 */
		// Split on white spaces in the line to get words
		TokenizerFactory tokenizer = new DefaultTokenizerFactory();
		tokenizer.setTokenPreProcessor(new CommonPreprocessor());
		
		/**
		 * 모델 학습하기
		 */
//		int batchSize = 1000;
//		int iterations = 3;
//		int layerSize = 150;
		int batchSize = 100000;
		int iterations = 3;
		int layerSize = 150;

		log.info("Building model....");
		vec = new Word2Vec.Builder()
			.batchSize(batchSize) //# words per minibatch.
			.minWordFrequency(3) //
			.useAdaGrad(false) //
			.layerSize(layerSize) // word feature vector size
			.iterations(iterations) // # iterations to train
			.learningRate(0.025) //
			.minLearningRate(1e-3) // learning rate decays wrt # words. floor learning
			.negativeSample(10) // sample size 10 words
			.iterate(iter) //
			.tokenizerFactory(tokenizer)
			.build();
		vec.fit();
		
		return "Building vector success!!!";
	}
	
	/**
	 * 학습 모델 저장
	 * @return
	 */
	@RequestMapping(value = "/saveLearntModel", method = RequestMethod.GET)
	@ResponseBody
	public Object saveLearntModel() {
		log.info("Saving learnt model to a file.");
		// Write word vectors
		try {
			WordVectorSerializer.writeWordVectors(vec, "word_vectors.txt");
		} catch (IOException e) {
			log.error("{}", e);
		}
		log.info("Model has been saved to a file.");
		return "Saved learnt model successfully.";
	}
	
	/**
	 * 학습 모델 테스트 1 - 관련도가 높은 단어 추출하기
	 * @return
	 */
	@RequestMapping(value = "/getNearestWords", method = RequestMethod.GET)
	@ResponseBody
	public void getNearestWords(int numOfRelatedWords, String word) {
		
		/*
		 * 특정 단어와 관련도가 높은 단어 추출하기
		 */
		Collection<String> lstNearest = vec.wordsNearest(word, numOfRelatedWords);
		log.info("Closest words of '{}' : {}", word, lstNearest);

		return ;
	}
	
	/**
	 * 학습 모델 테스트 2 - 유사도가 높은 단어 추출하기
	 * @return
	 */
	@RequestMapping(value = "/getSimilarWords", method = RequestMethod.GET)
	@ResponseBody
	public Collection<String> getSimilarWords(double accuracy, String word) {
		/*
		 * 특정 단어와 정확도(유사도)(?)가 높은 단어 추출하기
		 */
		Collection<String> lstSimilar = vec.similarWordsInVocabTo(word, accuracy);
		log.info("Similar words of '{}' by accuracy of {} : {}", word, accuracy, lstSimilar);
		return lstSimilar;
	}
	
	/**
	 * 학습 모델 테스트 3 - 특정 단어들과 가까우면서 특정 단어들과는 먼 단어 추출하기
	 * @return
	 */
	@RequestMapping(value = "/getNearestAndFarWords", method = RequestMethod.POST)
	@ResponseBody
	public Collection<String> getNearestWordsPosNeg(@RequestBody PosNegDto posNegWords) {
		/*
		 * 특정 단어와 가까운(?) 단어 추출하기
		 */
		Collection<String> nearestWords = vec.wordsNearest(posNegWords.getPositiveWords(), posNegWords.getNegativeWords(), posNegWords.getNumOfWords());
		log.info("Neareast words of '{}' and far from '{}' : {}", posNegWords.getPositiveWords(), posNegWords.getNegativeWords(), nearestWords);
		
		return nearestWords;
	}
	
	/**
	 * 학습 모델 테스트 4 - 정확도 판단하기
	 * <p>
	 * Input is list that contains several strings that has 4 words each with space separated.<br>
	 * 1st word: query word<br>
	 * 2~3rd word: negative words<br>
	 * 4th word: predicted nearest word
	 */
	@RequestMapping(value = "/getAccuracy", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Double> getAccuracy(@RequestBody AccuracyDto acryDto) {
		
		log.info("{}", acryDto.getQuestions());
		Map<String, Double> accuracyResult = vec.accuracy(acryDto.getQuestions());
		log.info("accuracyResult: {}", accuracyResult);
		return accuracyResult;
	}
	
}
