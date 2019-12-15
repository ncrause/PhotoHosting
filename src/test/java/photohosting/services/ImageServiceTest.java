/*
 * Copyright (C) 2020 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package photohosting.services;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import photohosting.services.beans.Images;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class ImageServiceTest {
	
	public ImageServiceTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	private ImageService subject;
	
	@Before
	public void setUp() {
		subject = new ImageService();
	}
	
	@After
	public void tearDown() {
	}

//	@Test
//	public void testPrepare() throws Exception {
//		System.out.println("prepare");
//		URL source = null;
//		ImageService instance = new ImageService();
//		Images expResult = null;
//		Images result = instance.prepare(source);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testScale_BufferedImage() {
//		System.out.println("scale");
//		BufferedImage image = null;
//		ImageService instance = new ImageService();
//		BufferedImage expResult = null;
//		BufferedImage result = instance.scale(image);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testScale_BufferedImage_Dimension() {
//		System.out.println("scale");
//		BufferedImage image = null;
//		Dimension target = null;
//		ImageService instance = new ImageService();
//		BufferedImage expResult = null;
//		BufferedImage result = instance.scale(image, target);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testToJPG() throws Exception {
//		System.out.println("toJPG");
//		BufferedImage image = null;
//		int compression = 0;
//		ImageService instance = new ImageService();
//		byte[] expResult = null;
//		byte[] result = instance.toJPG(image, compression);
//		assertArrayEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}
//
//	@Test
//	public void testCalculate2MB() {
//		System.out.println("calculate2MB");
//		Dimension size = null;
//		ImageService instance = new ImageService();
//		Dimension expResult = null;
//		Dimension result = instance.calculate2MB(size);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}

	@Test
	public void testCalculateOG() {
		System.out.println("calculateOG");
		Dimension result = subject.calculateOG(new Dimension(1900, 1200));
		assertEquals(600, result.width);
		assertEquals(600, result.height);
	}

//	@Test
//	public void testCalculateReddit() {
//		System.out.println("calculateReddit");
//		Dimension size = null;
//		ImageService instance = new ImageService();
//		Dimension expResult = null;
//		Dimension result = instance.calculateReddit(size);
//		assertEquals(expResult, result);
//		fail("The test case is a prototype.");
//	}

	@Test
	public void testCalculateTwitter() {
		System.out.println("calculateOG");
		
		Dimension result = subject.calculateTwitter(new Dimension(1900, 1200));
		
		assertEquals(600, result.width);
		assertEquals(300, result.height);
	}

	@Test
	public void testCalculateScaled_wideSource_squareTarget() {
		System.out.println("calculateScaled with a wide source and a square target");
		
		Dimension size = new Dimension(3000, 1000);
		Dimension target = new Dimension(600, 600);
		Dimension result = subject.calculateScaled(size, target);
		
		assertEquals(1800, result.width);
		assertEquals(600, result.height);
	}

	@Test
	public void testCalculateScaled_tallSource_squareTarget() {
		System.out.println("calculateScaled with a tall source and a square target");
		
		Dimension size = new Dimension(1000, 3000);
		Dimension target = new Dimension(600, 600);
		Dimension result = subject.calculateScaled(size, target);
		
		assertEquals(600, result.width);
		assertEquals(1800, result.height);
	}

	@Test
	public void testCalculateScaled_wideSource_tallTarget() {
		System.out.println("calculateScaled with a wide source and a tall target");
		
		Dimension size = new Dimension(3000, 1000);
		Dimension target = new Dimension(400, 800);
		Dimension result = subject.calculateScaled(size, target);
		
		assertThat(result.width, is(greaterThan(400)));
		assertEquals(800, result.height);
	}

	@Test
	public void testCalculateScaled_wideSource_wideTarget() {
		System.out.println("calculateScaled with a wide source and a wide target");
		
		Dimension size = new Dimension(3000, 1000);
		Dimension target = new Dimension(800, 400);
		Dimension result = subject.calculateScaled(size, target);
		
		assertThat(result.width, is(greaterThan(800)));
		assertEquals(400, result.height);
	}
	
}
