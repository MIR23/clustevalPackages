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
/**
 * 
 */
package de.clusteval.paramOptimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import junit.framework.Assert;
import junitx.framework.ArrayAssert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.ArraysExt;
import ch.qos.logback.classic.Level;
import de.clusteval.data.DataConfig;
import de.clusteval.framework.ClustevalBackendServer;
import de.clusteval.framework.repository.InvalidRepositoryException;
import de.clusteval.framework.repository.RegisterException;
import de.clusteval.framework.repository.Repository;
import de.clusteval.framework.repository.RepositoryAlreadyExistsException;
import de.clusteval.framework.repository.config.RepositoryConfigNotFoundException;
import de.clusteval.framework.repository.config.RepositoryConfigurationException;
import de.clusteval.program.ProgramConfig;
import de.clusteval.program.ProgramParameter;
import de.clusteval.quality.QualityMeasure;
import de.clusteval.quality.QualityMeasureValue;
import de.clusteval.quality.QualitySet;
import de.clusteval.quality.UnknownQualityMeasureException;
import de.clusteval.run.ParameterOptimizationRun;
import de.clusteval.run.Run;
import de.clusteval.run.result.RunResultParseException;
import de.clusteval.utils.InternalAttributeException;

/**
 * @author Christian Wiwie
 * 
 */
public class TestParameterOptimizationMethod {

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

	/**
	 * @throws FileNotFoundException
	 * @throws RepositoryAlreadyExistsException
	 * @throws InvalidRepositoryException
	 * @throws RepositoryConfigNotFoundException
	 * @throws RepositoryConfigurationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws RegisterException
	 */
	@Test
	public void testLayered() throws FileNotFoundException,
			RepositoryAlreadyExistsException, InvalidRepositoryException,
			RepositoryConfigNotFoundException,
			RepositoryConfigurationException, SecurityException,
			IllegalArgumentException, RegisterException {

		ClustevalBackendServer.logLevel(Level.INFO);
		Repository repo = new Repository(
				new File("repository").getAbsolutePath(), null);
		repo.initialize();

		int[] iters = ArraysExt.rep((int) Math.pow(100, 0.5), 2);
		LayeredDivisiveParameterOptimizationMethod method = new LayeredDivisiveParameterOptimizationMethod(
				repo, false, System.currentTimeMillis(), new File("bla"), null,
				null, null, null, null, iters, false);
		method.remainingIterationCount = 100;

		Assert.assertEquals(100, method.remainingIterationCount);

		Assert.assertEquals(100, method.getTotalIterationCount());

		int[] newIterations = method.getNextIterationsPerParameter();
		ArrayAssert.assertEquals(new int[]{5, 5}, newIterations);
		Assert.assertEquals(75, method.remainingIterationCount);

		newIterations = method.getNextIterationsPerParameter();
		ArrayAssert.assertEquals(new int[]{5, 5}, newIterations);
		Assert.assertEquals(50, method.remainingIterationCount);

		newIterations = method.getNextIterationsPerParameter();
		ArrayAssert.assertEquals(new int[]{5, 5}, newIterations);
		Assert.assertEquals(25, method.remainingIterationCount);

		newIterations = method.getNextIterationsPerParameter();
		ArrayAssert.assertEquals(new int[]{5, 5}, newIterations);
		Assert.assertEquals(0, method.remainingIterationCount);
	}

	/**
	 * @throws FileNotFoundException
	 * @throws RepositoryAlreadyExistsException
	 * @throws InvalidRepositoryException
	 * @throws RepositoryConfigNotFoundException
	 * @throws RepositoryConfigurationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws RegisterException
	 * @throws InternalAttributeException
	 * @throws ParameterOptimizationException
	 * @throws RunResultParseException
	 * @throws NoParameterSetFoundException
	 * @throws UnknownQualityMeasureException
	 */
	@Test
	public void testLayered2() throws FileNotFoundException,
			RepositoryAlreadyExistsException, InvalidRepositoryException,
			RepositoryConfigNotFoundException,
			RepositoryConfigurationException, SecurityException,
			IllegalArgumentException, RegisterException,
			InternalAttributeException, RunResultParseException,
			ParameterOptimizationException, NoParameterSetFoundException,
			UnknownQualityMeasureException {

		ClustevalBackendServer.logLevel(Level.INFO);
		Repository repo = new Repository(
				new File("repository").getAbsolutePath(), null);
		repo.initialize();

		ProgramConfig programConfig = repo
				.getProgramConfigWithName("gedevo.config");
		DataConfig dataConfig = repo
				.getDataConfigWithName("rashid_merge.dataconfig");
		Run run = new ParameterOptimizationRun(repo, null, 0, new File("bla"),
				new ArrayList<ProgramConfig>(), new ArrayList<DataConfig>(),
				new ArrayList<QualityMeasure>(),
				new ArrayList<Map<ProgramParameter<?>, String>>(), null,
				new ArrayList<ParameterOptimizationMethod>());

		int[] iters = ArraysExt.rep((int) Math.pow(100, 0.5), 2);
		LayeredDivisiveParameterOptimizationMethod method = new LayeredDivisiveParameterOptimizationMethod(
				repo, false, System.currentTimeMillis(), new File("bla"),
				(ParameterOptimizationRun) run, programConfig, dataConfig,
				programConfig.getOptimizableParams(),
				QualityMeasure.parseFromString(repo,
						"AlignedEdgesQualityMeasure"), iters, false);

		method.reset(new File("repository").getAbsoluteFile());

		QualitySet qualSet = new QualitySet();
		qualSet.put(QualityMeasure.parseFromString(repo,
				"AlignedEdgesQualityMeasure"), QualityMeasureValue
				.getForDouble(0.0));
		qualSet.put(QualityMeasure.parseFromString(repo,
				"GraphEditDistanceQualityMeasure"), QualityMeasureValue
				.getForDouble(0.0));
		qualSet.put(QualityMeasure.parseFromString(repo,
				"EdgeCorrectnessQualityMeasure"), QualityMeasureValue
				.getForDouble(0.0));

		Assert.assertEquals(100, method.getTotalIterationCount());
		Assert.assertEquals(3, method.layerCount);
		Assert.assertEquals(100, method.remainingIterationCount);

		// first layer
		method.next();
		method.giveQualityFeedback(qualSet);
		Assert.assertEquals(25,
				method.currentDivisiveMethod.getTotalIterationCount());

		while (method.currentCount < 25) {
			Assert.assertEquals(true, method.hasNext());
			method.next();
			method.giveQualityFeedback(qualSet);
			System.out.println(method.currentCount);
		}
		// second layer
		Assert.assertEquals(25,
				method.currentDivisiveMethod.getTotalIterationCount());
		while (method.currentCount < 50) {
			Assert.assertEquals(true, method.hasNext());
			method.next();
			method.giveQualityFeedback(qualSet);
			System.out.println(method.currentCount);
		}
		// third layer
		Assert.assertEquals(49,
				method.currentDivisiveMethod.getTotalIterationCount());
		while (method.currentCount < 98) {
			Assert.assertEquals(true, method.hasNext());
			method.next();
			method.giveQualityFeedback(qualSet);
			System.out.println(method.currentCount);
		}
		// last iteration is a skipping iteration

		Assert.assertEquals(true, method.hasNext());
		try {
			method.next();
		} catch (NoParameterSetFoundException e) {

		}
		method.giveQualityFeedback(qualSet);
		System.out.println(method.currentCount);

		Assert.assertEquals(0, method.remainingIterationCount);
	}

	/**
	 * @throws FileNotFoundException
	 * @throws RepositoryAlreadyExistsException
	 * @throws InvalidRepositoryException
	 * @throws RepositoryConfigNotFoundException
	 * @throws RepositoryConfigurationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws RegisterException
	 * @throws InternalAttributeException
	 * @throws ParameterOptimizationException
	 * @throws RunResultParseException
	 * @throws NoParameterSetFoundException
	 * @throws UnknownQualityMeasureException
	 */
	@Test
	public void testDivisiveStringParam() throws FileNotFoundException,
			RepositoryAlreadyExistsException, InvalidRepositoryException,
			RepositoryConfigNotFoundException,
			RepositoryConfigurationException, SecurityException,
			IllegalArgumentException, RegisterException,
			InternalAttributeException, RunResultParseException,
			ParameterOptimizationException, NoParameterSetFoundException,
			UnknownQualityMeasureException {

		ClustevalBackendServer.logLevel(Level.INFO);
		Repository repo = new Repository(
				new File("repository").getAbsolutePath(), null);
		repo.initialize();

		ProgramConfig programConfig = repo
				.getProgramConfigWithName("spinal.config");
		DataConfig dataConfig = repo
				.getDataConfigWithName("rashid_merge.dataconfig");
		Run run = new ParameterOptimizationRun(repo, null, 0, new File("bla"),
				new ArrayList<ProgramConfig>(), new ArrayList<DataConfig>(),
				new ArrayList<QualityMeasure>(),
				new ArrayList<Map<ProgramParameter<?>, String>>(), null,
				new ArrayList<ParameterOptimizationMethod>());

		int[] iters = ArraysExt.rep((int) Math.pow(100, 0.5), 2);
		DivisiveParameterOptimizationMethod method = new DivisiveParameterOptimizationMethod(
				repo, false, System.currentTimeMillis(), new File("bla"),
				(ParameterOptimizationRun) run, programConfig, dataConfig,
				programConfig.getOptimizableParams(),
				QualityMeasure.parseFromString(repo,
						"AlignedEdgesQualityMeasure"), iters, false);

		method.reset(new File("repository").getAbsoluteFile());

		Assert.assertEquals(20, method.getTotalIterationCount());

		for (ProgramParameter<?> param : method.parameterValues.keySet())
			ArraysExt.print(method.parameterValues.get(param));
		ArraysExt.print(method.iterationPerParameter);
	}
}
