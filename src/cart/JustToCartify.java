package cart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class JustToCartify
{
	// private static final int N_THREADS = 4;

	public static void main(String[] args) throws IOException
	{

		if (args.length < 1)
		{
			System.out.println("First argument is the filename to be cartified.");
			return;
		}

		originalDatabaseFilename = args[0];
		int k = 100;

		System.out.println(originalDatabaseFilename
				+ " is going to be cartified for separate dimensions.");

		cartifyIt(k);
	}

	public static String cartify(String databaseFileName, int k)
	{
		originalDatabaseFilename = databaseFileName;
		try
		{
			return cartifyIt(k);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private static String cartifyIt(int k) throws FileNotFoundException,
			IOException
	{

		readOriginalDatabase();
		createCartifiedFileNames();
		createCarts();

		return mergeToFinalFile(k);
	}

	private static String mergeToFinalFile(int k) throws FileNotFoundException,
			IOException
	{
		String mergedDbName = originalDatabaseFilename + "_k" + k + ".cart";
		File mergedDbFile = new File(mergedDbName);
		if (!mergedDbFile.exists())
		{
			PrintWriter writer = new PrintWriter(mergedDbFile);
			for (String fileName : cartifiedFilenames)
			{
				Scanner sc = new Scanner(new BufferedInputStream(new GZIPInputStream(new FileInputStream(new File(fileName)))));
				while (sc.hasNextLine())
				{
					String line = sc.nextLine();
					String[] lineArr = line.split(" ");
					StringBuilder trimmedLine = new StringBuilder();
					for (int i = 0; i < k; i++)
					{
						trimmedLine.append(lineArr[i]).append(" ");
					}
					writer.println(trimmedLine.toString());
				}
				sc.close();
			}
			writer.close();
		}
		return mergedDbName;
	}

	private static void readOriginalDatabase() throws FileNotFoundException
	{
		originalDatabase = new ArrayList<List<Double>>();

		Scanner sc = new Scanner(new File(originalDatabaseFilename));
		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
			String delimiter = " ";
			String[] lineArr = line.split(delimiter);

			List<Double> thisRow = new ArrayList<Double>(lineArr.length);
			for (String featStr : lineArr)
			{
				thisRow.add(Double.valueOf(featStr));
			}
			originalDatabase.add(thisRow);
		}
		sc.close();

		int numOfDims = originalDatabase.get(0).size();
		dimensions = new int[numOfDims][1];

		for (int i = 0; i < numOfDims; i++)
		{
			dimensions[i][0] = i;
		}
	}

	private static List<String> cartifiedFilenames;
	private static int[][] dimensions;
	private static List<List<Double>> originalDatabase;
	private static String originalDatabaseFilename;

	public static void createCartifiedFileNames()
	{
		cartifiedFilenames = new LinkedList<String>();

		for (int[] dimension : dimensions)
		{
			String dimensionString = "";
			if (dimension.length > 20)
			{
				dimensionString = dimension[0] + "_" + dimension[dimension.length - 1]
						+ ",";
			} else
			{
				for (int d : dimension)
				{
					dimensionString += d + ",";
				}
			}
			cartifiedFilenames.add(originalDatabaseFilename + "-m["
					+ dimensionString.substring(0, dimensionString.length() - 1)
					+ "].cart.gz");
		}
	}

	public static void createCarts()
	{
		final Cartifier cartifier = new Cartifier(originalDatabase);
		ExecutorService executor = Executors.newFixedThreadPool(CartiClus.N_Threads);

		int dimCounter = 0;
		for (final String filename : cartifiedFilenames)
		{
			final int[] dimension = dimensions[dimCounter++];
			if (!(new File(filename).exists()))
			{
				executor.execute(new Runnable() {
					@Override
					public void run()
					{
						cartifier.cartifyNumeric(dimension, filename);
					}
				});
			}
		}
		try
		{
			executor.shutdown();
			executor.awaitTermination(1000, TimeUnit.MINUTES);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			executor.shutdownNow();
		}
	}
}
