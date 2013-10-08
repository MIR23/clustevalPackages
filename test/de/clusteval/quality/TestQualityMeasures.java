package de.clusteval.quality;
/*******************************************************************************
 * Copyright (c) 2013 Christian Wiwie.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Christian Wiwie - initial API and implementation
 ******************************************************************************/
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.qos.logback.classic.Level;

import utils.Pair;
import utils.Triple;
import de.clusteval.data.DataConfig;
import de.clusteval.data.dataset.DataSet;
import de.clusteval.data.dataset.DataSetConfig;
import de.clusteval.data.dataset.format.InvalidDataSetFormatVersionException;
import de.clusteval.data.dataset.format.UnknownDataSetFormatException;
import de.clusteval.data.goldstandard.format.UnknownGoldStandardFormatException;
import de.clusteval.framework.ClustevalBackendServer;
import de.clusteval.framework.repository.InvalidRepositoryException;
import de.clusteval.framework.repository.RegisterException;
import de.clusteval.framework.repository.Repository;
import de.clusteval.framework.repository.RepositoryAlreadyExistsException;
import de.clusteval.framework.repository.config.RepositoryConfigNotFoundException;
import de.clusteval.framework.repository.config.RepositoryConfigurationException;
import de.clusteval.graphmatching.GraphMatching;
import de.clusteval.quality.QualityMeasure;
import de.clusteval.quality.UnknownQualityMeasureException;
import de.clusteval.utils.RCalculationException;
import de.clusteval.utils.RNotAvailableException;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * 
 */

/**
 * @author Christian Wiwie
 * 
 */
public class TestQualityMeasures {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetQualityOf() throws RepositoryAlreadyExistsException,
			InvalidRepositoryException, RepositoryConfigNotFoundException,
			RepositoryConfigurationException, UnknownQualityMeasureException,
			UnknownGoldStandardFormatException, UnknownDataSetFormatException,
			InvalidDataSetFormatVersionException, RNotAvailableException,
			RCalculationException, IOException, RegisterException {
		ClustevalBackendServer.logLevel(Level.INFO);
		Repository repo = new Repository("repository", null);
		repo.initialize();

		DirectedSparseMultigraph<String, String> graph1 = new DirectedSparseMultigraph<String, String>();
		graph1.addVertex("n1");
		graph1.addVertex("n2");
		graph1.addVertex("n3");
		graph1.addEdge("e1", "n1", "n2");
		DirectedSparseMultigraph<String, String> graph2 = new DirectedSparseMultigraph<String, String>();
		graph2.addVertex("v1");
		graph2.addVertex("v2");
		graph2.addVertex("v3");
		graph2.addEdge("e1", "v1", "v3");

		// create testcase datasetconfig
		DataSetConfig dsc = new DataSetConfig(repo, System.currentTimeMillis(),
				new File("test"), new LinkedList<String>(
						Arrays.asList(new String[]{})),
				new ArrayList<Triple<String, DataSet, String>>());
		// init graphs attribute of dsc
		dsc.loadIntoMemory();
		// add graphs to dsc
		dsc.getGraphs().add(graph1);
		dsc.getGraphs().add(graph2);
		// create dataconfig
		DataConfig dc = new DataConfig(repo, System.currentTimeMillis(),
				new File("test"), dsc, null);

		GraphMatching gm = new GraphMatching();

		gm.addMatching(Pair.getPair("n1", "v1"));
		gm.addMatching(Pair.getPair("n2", "v3"));
		gm.addMatching(Pair.getPair("n3", "v2"));

		// compare expected quality with calculated quality
		QualityMeasure alignedEdges = QualityMeasure.parseFromString(repo,
				"AlignedEdgesQualityMeasure");
		QualityMeasure edgeCorrectness = QualityMeasure.parseFromString(repo,
				"EdgeCorrectnessQualityMeasure");
		QualityMeasure ged = QualityMeasure.parseFromString(repo,
				"GraphEditDistanceQualityMeasure");
		Assert.assertEquals(1.0, alignedEdges.getQualityOf(gm, null, dc)
				.getValue());
		Assert.assertEquals(0.0, ged.getQualityOf(gm, null, dc).getValue());
		Assert.assertEquals(100.0, edgeCorrectness.getQualityOf(gm, null, dc)
				.getValue());

		// modify graph and test again; insert additional edges/nodes
		graph1.addEdge("e2", "n1", "n3");
		Assert.assertEquals(1.0, alignedEdges.getQualityOf(gm, null, dc)
				.getValue());
		Assert.assertEquals(1.0, ged.getQualityOf(gm, null, dc).getValue());
		Assert.assertEquals(100.0, edgeCorrectness.getQualityOf(gm, null, dc)
				.getValue());

		// modify graph and test again; insert additional edges/nodes
		graph2.addEdge("e2", "v2", "v3");
		Assert.assertEquals(1.0, alignedEdges.getQualityOf(gm, null, dc)
				.getValue());
		Assert.assertEquals(2.0, ged.getQualityOf(gm, null, dc).getValue());
		Assert.assertEquals(50.0, edgeCorrectness.getQualityOf(gm, null, dc)
				.getValue());
	}
}
