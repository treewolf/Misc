
namespace AddressLabel
{
	/// <summary>
	/// Description of MainForm.
	/// </summary>
	public partial class MainForm : System.Windows.Forms.Form
	{
		public MainForm()
		{
			//
			// The InitializeComponent() call is required for Windows Forms designer support.
			//
			InitializeComponent();
		}
		
		// Click control for when choosing a file
		private void chooseFile_Click(System.Object sender, System.EventArgs e){
			System.Windows.Forms.OpenFileDialog dialogOpen = new System.Windows.Forms.OpenFileDialog();
			dialogOpen.RestoreDirectory = true;
			
			// Shows dialog
			System.Windows.Forms.DialogResult result = dialogOpen.ShowDialog();
			
			if(result == System.Windows.Forms.DialogResult.OK){
				System.String fileName = dialogOpen.FileName;
				this.labelPointer.Text = fileName;
								
				// Check if file is valid
				System.String [] temp = fileName.Split(new System.Char [] {'.'});
				System.String extens = temp[temp.Length - 1];
				if(!extens.Equals("csv")){
					this.labelPointer.Text = "Invalid file. Choose a .csv file or save your file as a .csv";
					this.createText.Enabled = false;
				}
				else{
					this.createText.Enabled = true;
				}
			}
		}
		
		// Click control to create a text file with formatted text.
		private void create_Click(System.Object sender, System.EventArgs e){
			
			// Edit input file to create an output file .txt
			System.String fileName = this.labelPointer.Text;
			System.String outFileName = "";
			System.String [] temp = fileName.Split(new System.Char [] {'.'});
			if(temp.Length > 2){
				for(int i = 0; i < temp.Length - 1; ++i){
					outFileName += temp[i].ToString();
				}
			}
			else{
				outFileName = temp[0].ToString();
			}
			outFileName += ".txt";
			
			System.IO.StreamWriter fw = new System.IO.StreamWriter(outFileName);
			using(System.IO.StreamReader sr = new System.IO.StreamReader(fileName)){
				System.String line;
				bool hitBOF = false;
				while((line = sr.ReadLine()) != null){
					if(hitBOF == true){
						if(line.Contains("EOF")){
							break;
						}
						
						// Sends specific data to label maker method
						System.String [] elements = line.Split(new System.Char [] {','});
						System.String addressee = elements[7].ToString();
						System.String zip = elements[6].ToString();
						System.String street = elements[5].ToString();
						System.String city = elements[4].ToString();
						System.String state = elements[3].ToString();
						System.String members = elements[2].ToString();
						System.String lastName = elements[0].ToString();
						System.String formatted = labelFormat(lastName, members, street, city, state, zip, addressee);
						
						// Writes to a text file
						fw.WriteLine(formatted + "\n");
					}
					else{
						if(line.Contains("BOF")){
							hitBOF = true;
						}
					}
				}
				fw.Close();
				this.labelPointer.Text = "";
				this.createText.Enabled = false;
				System.Windows.Forms.MessageBox.Show("Label created successfully.", "Alert");
			}
		}
		
		// Method is called to format a label
		private System.String labelFormat(System.String lastName, System.String members, System.String street, System.String city, System.String state, System.String zip, System.String addressee){
			// With more flags, and params, need to pass in lists instead of individual vars.--------
		
			// sort into lines for easiness
			System.String line1 = addressee;
			System.String line2 = street;
			System.String line3 = city + ", " + state + " " + zip;
			
			// Justifies based on flag
			System.String justify = "";
			switch(flag_justify){
				case "Left":
					justify =
						line1 + "\n" + line2 + "\n" + line3;
					break;
				case "Center":
					System.String [] temp = {line1, line2, line3};
					
					int maxIndex = 0;
					int maxLength = line1.Length;
					for(int i = 0; i < temp.Length; ++i){
						if(temp[i].Length > maxLength){
							maxIndex = i;
							maxLength = temp[i].Length;
						}
					}
					maxLength += 1;
					
					// Manually do spaces, padleft isnt working
					System.String [] adjustedTemp = new System.String[3];
					int tempIndex = 0;
					foreach(System.String s in temp){
						int spaces = ((maxLength - s.Length) / 2) - 1;
						System.String spaceString = "";
						for(int i = spaces; i > 0; --i){
							spaceString += " ";
						}
						adjustedTemp[tempIndex] = spaceString + s;
						++tempIndex;
					}
					
					justify = adjustedTemp[0].ToString() + "\n" + adjustedTemp[1].ToString() + "\n" + adjustedTemp[2].ToString();
					
					break;
				case "Right":
					justify = "";
					break;
				default:
					break;
			}
			return justify;			
		}
		
		// Radio button tracker for justification
		// Adjusts justify's flag.
		private void justify_changeSelected(System.Object sender, System.EventArgs e){
			System.Windows.Forms.RadioButton rb = sender as System.Windows.Forms.RadioButton;
			flag_justify = rb.Text;			
		}
	}
}
