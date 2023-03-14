import java.util.Scanner;
import java.util.*;


public class AG {
	

	public static void main(String[] args) {
		System.out.println("Enter number of process: ");
		Scanner sc = new Scanner(System.in);
		processNum = sc.nextInt();
		Process[] array = new Process[processNum];
		for (int i = 0; i < processNum; i++) {
			System.out.println("Enter a Process name");
			String name = sc.next();
			System.out.println("Enter process Burst Time");
			int burst = sc.nextInt();
			System.out.println("Enter process Arrival Time");
			int arrival = sc.nextInt();
			System.out.println("Enter process Priority Time");
			int priority = sc.nextInt();
			System.out.println("Enter process Quantum Time");
			int qunatum = sc.nextInt();
			array[i] = new Process(name, burst, arrival, priority, qunatum);

		}
		// sorting the array
		for (int i = 1; i < processNum; ++i) {
			Process pp = array[i];
			int j = i - 1;
			while (j >= 0 && array[j].arrivalTime > pp.arrivalTime) {
				array[j + 1] = array[j];
				j = j - 1;
			}
			array[j + 1] = pp;
		}

		currentTime = 0;
		curInput = 0;
		int m = 0;
		Process current = null;
		ArrayList<Process> queue = new ArrayList<Process>();

		while (m < processNum) {
			isProcess(array, queue);

			if (queue.isEmpty() && current == null) {
				currentTime++;
				continue;
			}

			if (current == null) {
				current = queue.get(0);
				queue.remove(0);
				// modify total wait
				current.totalWait += (currentTime - current.arrival_Time);
			}

			// work first 25% 
			int used = (int) Math.ceil((double) current.quauntum * 1 / 4);
			currentTime += Math.min(used, current.Burst_time);
			current.Burst_time -= used;

			// dead check
			if (current.Burst_time <= 0) {
				m++;
				System.out.println(current.processName + " time= " + currentTime);
				current = null;
				continue;
			}

			isProcess(array, queue);

			// check priority 
			int temp = checkpriority(queue, current.priority);
			if (temp != -1) {
				//work 25%
				int rem = (int) Math.ceil((double) (current.quauntum - used) / (double) 2);
				//scenorio 3
				current.quauntum += rem;
				current.arrival_Time = currentTime;
				queue.add(current);
				System.out.println(current.processName + " time= " + currentTime);
				current = queue.get(temp);
				queue.remove(temp);

				// modify total wait
				current.totalWait += (currentTime - current.arrival_Time);
				continue;
			}
			isProcess(array, queue);

			// work till 50%
			int timeUsed = (int) Math.ceil((double) current.quauntum * 1 / 2);
			currentTime += Math.min((timeUsed - used), current.Burst_time);
			current.Burst_time -= (timeUsed - used);

			// dead check
			if (current.Burst_time <= 0) {
				m++;
				System.out.println(current.processName + " time= " + currentTime);
				current = null;
				continue;
			}

			// check Shortest job
			temp = checksjf(queue, current.Burst_time);
			if (temp != -1) {
				int rem = current.quauntum - timeUsed;
				current.quauntum += rem;
				System.out.println(current.processName + " time= " + currentTime);
				current.arrival_Time = currentTime;
				queue.add(current);
				current = queue.get(temp);
				queue.remove(temp);

				// modify total wait
				current.totalWait += (currentTime - current.arrival_Time);
				continue;
			}

			// work till the end of the quantum time
			currentTime += Math.min(current.quauntum - timeUsed, current.Burst_time);
			current.Burst_time -= (current.quauntum - timeUsed);
			System.out.println(current.processName + " time= " + currentTime);

			// dead check
			if (current.Burst_time <= 0)
				m++;
			else {
				current.quauntum = 2;
				current.arrival_Time = currentTime;
				queue.add(current);
			}
			current = null;
		}
		System.out.println("--------------------------------------");
		System.out.println("waiting time for each process:");

		// print waiting time
		int averageWait = 0;
		for (int i = 0; i < processNum; i++) {
			System.out.println(array[i].processName + " " + array[i].totalWait);
			averageWait += array[i].totalWait;
		}
		System.out.println("--------------------------------------");
		System.out.println("Turnaround time for each process:");
		// print Turnaround time
		int avgerageTurnaround = 0;
		for (int i = 0; i < processNum; i++) {
			System.out.println(array[i].processName + " " + (array[i].totalWait + array[i].burstTime));
			avgerageTurnaround += array[i].totalWait + array[i].burstTime;
		}
		System.out.println("--------------------------------------");
		System.out.println("Average wait time : " + (float) averageWait / processNum);
		System.out.println("Average TurnAround time : " + (float) avgerageTurnaround / processNum);
		System.out.println("--------------------------------------");
	}

	//return index of min priority in queue!
	public static int checkpriority(ArrayList<Process> queue, int currp) {
		int x = -1;
		int i = 0;
		for (Process p : queue) {
			if (p.priority < currp) {
				x = i;
				currp = p.priority;
			}
			i++;
		}
		return x;
	}

	public static int curInput = 0;
	static int processNum;
	static int currentTime;

	//check process arrive or not
	//put process that arrive in queue
	static void isProcess(Process[] arr, ArrayList<Process> qu) {
		while (true) {
			if (curInput >= processNum)
				break;
			if (arr[curInput].arrivalTime > currentTime)
				break;
			qu.add(arr[curInput++]);
		}
	}

	//put process that have less Burst time in queue
	public static int checksjf(ArrayList<Process> queue, int currt) {
		int x = -1;
		int i = 0;
		for (Process p : queue) {
			if (p.Burst_time < currt) {
				x = i;
				currt = p.Burst_time;
			}
			i++;
		}
		return x;
	}

}
