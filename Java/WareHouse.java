
public class WareHouse {
	private int S;
	private int L;
	private int M;
	
	public WareHouse(int S, int M, int L) {
		// TODO Auto-generated constructor stub
	
		this.S = S;
		this.L = L;
		this.M = M;
	}
	public void addSmall(){	//small packages added to warehouse A from truck
		S += 1;
	}

	public void addLarge(){	//large packages added to warehouse B from truck
		L += 1;
	}
	public void takeMedium(){	//number of medium packages is decreased when put into truck
		M -= 1;
	}
	public int medium()
	{
		return M;
	}
	public int large()
	{
		return L;
	}
	public int small()
	{
		return S;
	}
	public void takeSmall(){	//number of small packages in truck needs reduced when taken from truck
		S -= 1;
	}

	public void takeLarge(){	//number of large packages in truck needs reduced when taken from truck
		L -= 1;
	}
	public void addMedium(){	//number of medium packages is increased when put into truck
		M += 1;
	}
	public void print()
	{
		System.out.println("(" + S + ", " + M + ", " + L + ")");
		return;
	}
}
