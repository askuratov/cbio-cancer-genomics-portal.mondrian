package org.mskcc.mondrian.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataTypeMatrix {
	private static final String DEFAULT_KEY_ATTRIBUTE = "DEFAULT";
	protected int significanceType = 3;

	private Map<String, List<String>> infoMap;
	private Map<String, List<Double>> dataMap;
	
	private List<String> dataColNames;
	private List<String> infoColNames;
	
	public static enum DATA_TYPE {
		EXTENDED_MUTATION, RPPA, GENETIC_PROFILE_FORMAT1, GENETIC_PROFILE_FORMAT2
	}
	
	private String keyAttributeName = DEFAULT_KEY_ATTRIBUTE;
	
	/*
	 * the key attribute name is the attribute by which the expression data is
	 * matched to the node name.  For instance, this might be a commercial
	 * probe set ID.
	 */
	private boolean mappingByAttribute = false;
	int numRows;
	int numCols;
	
	public DataTypeMatrix(InputStream input, DATA_TYPE dataType) throws IOException {
		this(input, dataType, DEFAULT_KEY_ATTRIBUTE);		
	}
	
	public DataTypeMatrix(InputStream input, DATA_TYPE dataType, String keyAttributeName) throws IOException {
		numRows = 0;
		numCols = 0;
		this.keyAttributeName = keyAttributeName;
		this.initDataStructures();
		this.loadData(input, dataType);
	}

	/**
	 * Initializes all data structures.
	 */
	private void initDataStructures() {
		if (dataColNames != null) {
			dataColNames.clear();
		} else {
			dataColNames = new ArrayList<String>();
		}

		if (infoColNames != null) {
			infoColNames.clear();
		} else {
			infoColNames = new ArrayList<String>();
		}
		
		if (infoMap != null) {
			infoMap.clear();
		} else {
			infoMap = new HashMap<String, List<String>>();
		}
		
		if (dataMap != null) {
			dataMap.clear();
		} else {
			dataMap = new HashMap<String, List<Double>>();
		}
	}

	/**
	 * Loads the data from the specified input stream into memory.
	 *
	 * @param filename Name of Expression Data File.
	 * @return always returns true, indicating succesful load.
	 * @throws IOException Error loading / parsing the Expression Data File.
	 */
	public boolean loadData(InputStream input, DATA_TYPE dataType) throws IOException {
		if (dataType == DATA_TYPE.GENETIC_PROFILE_FORMAT1) {
			infoColNames.add("GENE_ID");
			infoColNames.add("COMMON");
			if (DEFAULT_KEY_ATTRIBUTE.equals(keyAttributeName)) keyAttributeName = "COMMON";
		} else if (dataType == DATA_TYPE.GENETIC_PROFILE_FORMAT2) {
			
		} else if (dataType == DATA_TYPE.EXTENDED_MUTATION) {
			
		} else if (dataType == DATA_TYPE.RPPA) {
			
		}

		if (input == null)
			return false;

		Scanner scanner = new Scanner(input);
		//String[] lines = rawText.split(System.getProperty("line.separator"));

		String headerLine = scanner.nextLine(); //lines[lineCount++];
		while (headerLine.startsWith("#")) {  // skip all the comments
			headerLine = scanner.nextLine();
		}

		if ((headerLine == null) || (headerLine.length() == 0))
			return false;
		
		StringTokenizer headerTok = new StringTokenizer(headerLine, "\t");

		/* eat the first tokens from the header line */
		for (int i = 0; i < infoColNames.size(); i++) {
			headerTok.nextToken();
		}
		
		/* the rest tokens are the data column names */
		while (headerTok.hasMoreTokens())
			dataColNames.add(headerTok.nextToken());

		while (scanner.hasNext()) {
			parseOneLine(scanner.nextLine(), keyAttributeName);
		}
		
		if (input != null) {
			try { input.close(); } catch (IOException ex) {}
		}
		return true;
	}

	private void parseOneLine(String oneLine, String keyAttributeName)
	    throws IOException {
		// 
		// Step 1: divide the line into input tokens, and parse through
		// the input tokens.
		//
		StringTokenizer strtok = new StringTokenizer(oneLine);
		int numTokens = strtok.countTokens();

		if (numTokens == 0) {
			return;
		}
		
		String key = null;
		List<String> infoList = new ArrayList<String>();
		/* first few tokens are info tokens */
		for (int i = 0 ; i < infoColNames.size(); i++) {
			String tok = strtok.nextToken();
			infoList.add(tok);
			if (infoColNames.get(i).equals(keyAttributeName)) 
				key = tok;
		}
		
		List<Double> dataList = new ArrayList<Double>();
		/* the rest tokens are data */
		while (strtok.hasMoreTokens()) {
			String tok = strtok.nextToken();
			dataList.add(Double.parseDouble(tok));
		}
		
		infoMap.put(key, infoList);
		dataMap.put(key, dataList);
	} // parseOneLine
	
	public int getNumRows() {
		return dataMap.keySet().size();
	}
	
	public List<Double> getDataRow(String key) {
		return dataMap.get(key);
	}
	
	public List<String> getDataColNames() {
		return this.dataColNames;
	}

	/**
	 * Copies ExpressionData data structure into CyAttributes data structure.
	 *
	 * @param nodeAttribs Node Attributes Object.
	 * @param taskMonitor Task Monitor. Can be null.
	 */
	/*
	public void copyToAttribs(CyAttributes nodeAttribs, TaskMonitor taskMonitor) {
		String[] condNames = getConditionNames();

		for (int condNum = 0; condNum < condNames.length; condNum++) {
			String condName = condNames[condNum];
			String eStr = condName + "exp";
			String sStr = condName + "sig";

			for (int i = 0; i < geneNames.size(); i++) {
				String canName = (String) geneNames.get(i);

				mRNAMeasurement mm = getMeasurement(canName, condName);

				if (mm != null) {
					nodeAttribs.setAttribute(canName, eStr, new Double(mm.getRatio()));
					nodeAttribs.setAttribute(canName, sStr, new Double(mm.getSignificance()));
				}

				// Report on Progress to the Task Monitor.
				if (taskMonitor != null) {
					int currentCoordinate = (condNum * geneNames.size()) + i;
					int matrixSize = condNames.length * geneNames.size();
					double percent = ((double) currentCoordinate / matrixSize) * 100.0;
					taskMonitor.setPercentCompleted((int) percent);
				}
			}
		}
	}
	*/
}
