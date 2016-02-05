import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class demonstrates basic divide-conquer process.
 * 
 * @author Ron.Coleman
 *
 */
public class MobyDivideConquer implements Runnable
{
	public static final int TIMEOUT = 30 * 1000;

	public static void main(String[] args)
	{

		final File folder = new File("15-text");
		ArrayList<String> fileNames = new ArrayList<>();
		ArrayList<HashMap<String, Integer>> results = new ArrayList<>();

		for (File entry : folder.listFiles())
		{
			String path = entry.getPath();
			fileNames.add(path);
		}

		System.out.println("parent waiting for child...");

		long t0 = System.currentTimeMillis();

		int cores  = Runtime.getRuntime().availableProcessors();

		// Launch the child worker threads
		for (int id = 0; id < cores; id++)
		{
			MobyDivideConquer runnable = new MobyDivideConquer(fileNames, results);
			new Thread(runnable).start();
		}

		int done = 0;

		
		synchronized (fileNames)
		{
			try
			{
				while (done < cores)
				{
					// Wait but not indefinitely
					long start = System.currentTimeMillis();
					fileNames.wait(TIMEOUT);
					long end = System.currentTimeMillis();

					// If we've not been waiting too time, everything ok
					// otherwise
					// there may be a problem.
					if ((end - start) < TIMEOUT)
						done++;
					else
						break;
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		HashMap<String, Integer> totalFreq = new HashMap<>();
		while(done<cores)
		{
			synchronized (results)
			{
				try
				{
					results.wait(TIMEOUT);
					int sz=results.size();
					for( int i=0; i<sz;i++)
					{
						HashMap<String, Integer> castFreq = (HashMap<String, Integer>) results.get(i);
						castFreq.forEach((k, v) -> totalFreq.merge(k, v, (v1, v2) -> v1 + v2));

					}
					
					done+=sz;

					
					
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 		
			}
		}

		
		
		
		/*
		 while(done < cores)
		 {
		 	synchronized(results)
		 	{	
		 		results.wait();
		 		int sz=results.size();
		 		for( int i=0; i<sz;i++)
		 			merge(results.remove(0));
		 		
		 	}
		 } 
		 
		 */
/*		Object freqArr[] = results.toArray();
		HashMap<String, Integer> totalFreq = new HashMap<>();
		for (Object freq : freqArr)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> castFreq = (HashMap<String, Integer>) freq;
			castFreq.forEach((k, v) -> totalFreq.merge(k, v, (v1, v2) -> v1 + v2));

		}

		ArrayList<Pair> list = new ArrayList<>();

		for (String key : totalFreq.keySet())
		{
			list.add(new Pair(key, totalFreq.get(key)));
		}

		Collections.sort(list);
		int j = 0;
		list.remove(0);
		for (Pair p : list)
		{
			System.out.println(p.s + " " + p.i);
			if (++j == 10)
				break;
		}*/

		long t1 = System.currentTimeMillis();

		double totalWaitTime = (t1 - t0) / 1000.0;

		
		System.out.println(done + " / " + cores);
		if (done == cores)
			System.out.println("Parent detected " + done + " children done! waiting = " + totalWaitTime);
		else
			System.out.println((cores - done) + "children did not finish, results uncertain!");
		
		//
		//float T1=0.45f;
		
	}

	/** Local reference to shared work queue */
	private ArrayList<String> fileNames = null;

	/** Work item for this thread */
	private String workItem = "";

	/** Global id counter for child threads */
	private static int idCounter = 0;

	ArrayList<HashMap<String, Integer>> results = null;

	/** My id */
	private int id = -1;

	/**
	 * Constructor
	 * 
	 * @param fileNames
	 *            Work queue
	 */
	public MobyDivideConquer(ArrayList<String> fileNames, ArrayList<HashMap<String, Integer>> results)
	{
		this.id = idCounter++;
		this.fileNames = fileNames;
		this.results = results;
	}

	/**
	 * Transfers here when thread started
	 */
	@Override
	public void run()
	{
		// Loop until all work done.
		HashMap<String, Integer> frequency = new HashMap<>();
		while (true)
		{
			workItem = "";

			// / ENTERING critical region to safely manipulate the work queue
			synchronized (fileNames)
			{
				int sz = fileNames.size();

				if (sz != 0)
					workItem = fileNames.remove(0);
				else
					fileNames.notify();
			}
			// / EXITING critical region

			// If no work, we're done, otherwise do work OUTSIDE the critical
			// region
			// as we have our work and there no point risking deadlock.
			if (workItem == "")
				break;

			// System.out.println("child " + id + " working for " + workItem +
			// " file");
			frequency = doWork(frequency);

		}

		synchronized (results)
		{
			results.add(frequency);
			results.notify();			
		}
		
		System.out.println("child " + id + " done!");

	}

	private HashMap<String, Integer> doWork(HashMap<String, Integer> frequency)
	{
		FileInputStream in = null;
		BufferedReader br = null;
		try
		{
			in = new FileInputStream(workItem);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{
				String[] words = strLine.split(" +");

				for (int i = 0; i < words.length; i++)
				{
					if (frequency.get(words[i]) == null)
						frequency.put(words[i], 1);
					else
						frequency.put(words[i], (frequency.get(words[i]) + 1));
				}

			}

		} catch (Exception e)
		{
			System.out.println(e);
		} finally
		{
			try
			{
				br.close();
				in.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return frequency;
	}
}

class Pair implements Comparable<Pair>
{
	public final String s;
	public final Integer i;

	public Pair(String s, Integer i)
	{
		this.s = s;
		this.i = i;
	}

	@Override
	public int compareTo(Pair p)
	{
		// TODO Auto-generated method stub
		return p.i - this.i;
	}
}
