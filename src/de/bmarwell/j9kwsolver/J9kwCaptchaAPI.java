/**
 * Copyright (c) 2010-2012, Benjamin Marwell.  This file is
 * licensed under the Affero General Public License version 3 or later.  See
 * the COPYRIGHT file.
 */
package de.bmarwell.j9kwsolver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bmarwell.j9kwsolver.action.CaptchaGetThread;
import de.bmarwell.j9kwsolver.action.CaptchaSolveThread;
import de.bmarwell.j9kwsolver.domain.Captcha;
import de.bmarwell.j9kwsolver.domain.CaptchaSolution;
import de.bmarwell.j9kwsolver.response.CaptchaSolutionResponse;

/**
 * An API for sending and retrieving captchas.
 * @author Benjamin Marwell
 *
 */
public class J9kwCaptchaAPI {
	private static final Logger log = LoggerFactory.getLogger(J9kwCaptchaAPI.class);
	private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	/**
	 * Empty hidden default constructor
	 */
	private J9kwCaptchaAPI() {}
	
	public boolean shutdownExecutor() {
		singleThreadExecutor.shutdown();
		
		return true;
	}
	
	
	
	/**
	 * Requests and accepts a captcha from the server.
	 * @param tryLoop - set yes to loop until captcha is received.
	 * @return a captcha or null if none received (only possible with <code>tryLoop=false</code>.
	 */
	public Future<Captcha> getNewCaptcha(boolean tryLoop) {
		CaptchaGetThread gt = new CaptchaGetThread();
		
		log.trace("starting get-Task");
		Future<Captcha> result = singleThreadExecutor.submit(gt);
		
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public Future<CaptchaSolutionResponse> solveCaptcha(CaptchaSolution solution) {
		CaptchaSolveThread cst = new CaptchaSolveThread();
		cst.setSolution(solution);
		
		log.trace("starting solve thread");
		Future<CaptchaSolutionResponse> result = singleThreadExecutor.submit(cst);
		
		return result;
	}
	
	/**
	 * @return
	 */
	public static J9kwCaptchaAPI getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * @author Benjamin Marwell
	 *
	 */
	private static class SingletonHolder {
		private static J9kwCaptchaAPI instance = new J9kwCaptchaAPI();
	}
}
