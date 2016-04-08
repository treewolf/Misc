
namespace AddressLabel
{
	partial class MainForm
	{
		/// <summary>
		/// Designer variable used to keep track of non-visual components.
		/// </summary>
		private System.ComponentModel.IContainer components = null;
		
		/// <summary>
		/// Disposes resources used by the form.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing) {
				if (components != null) {
					components.Dispose();
				}
			}
			base.Dispose(disposing);
		}
		
		/// <summary>
		/// This method is required for Windows Forms designer support.
		/// Do not change the method contents inside the source code editor. The Forms designer might
		/// not be able to load this method if it was changed manually.
		/// </summary>
		private void InitializeComponent()
		{
			// 
			// MainForm
			// 
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Text = "AddressLabel by Treewolf";
			this.Name = "MainForm";
			this.MinimumSize = new System.Drawing.Size(400, 400);	// width, height
			this.MaximumSize = new System.Drawing.Size(400, 400);
			
			// Button to choose a file
			this.button = new System.Windows.Forms.Button();
			this.Controls.Add(this.button);
			this.button.Text = "Choose file";
			this.button.Location = new System.Drawing.Point(50, 60);
			this.button.Click += new System.EventHandler(chooseFile_Click);
			
			// Info label for chosen file
			this.label = new System.Windows.Forms.Label();
			this.Controls.Add(this.label);
			this.label.Text = "File: ";
			this.label.Size = label.PreferredSize;
			this.label.Location = new System.Drawing.Point(20, 40);
			
			// Label pointing to chosen file path
			this.labelPointer = new System.Windows.Forms.TextBox();
			this.Controls.Add(this.labelPointer);
			this.labelPointer.Text = "";
			this.labelPointer.Location = new System.Drawing.Point(50, 38);
			this.labelPointer.Size = new System.Drawing.Size(300,labelPointer.Height);
			this.labelPointer.ReadOnly = true;
			
			// When clicked, creates text file with formats
			this.createText = new System.Windows.Forms.Button();
			this.Controls.Add(this.createText);
			this.createText.Text = "Create my label";
			this.createText.Enabled = false;
			this.createText.AutoSize = true;
			this.createText.Location = new System.Drawing.Point(150, 290);
			this.createText.Click += new System.EventHandler(create_Click);
			
			// This groupbox is root groupbox for other groups
			this.mainGroup = new System.Windows.Forms.GroupBox();
			this.Controls.Add(this.mainGroup);
			this.mainGroup.Size = new System.Drawing.Size(345, 150);	// width, height
			this.mainGroup.Location = new System.Drawing.Point(20, 110);
			
			// Group holds radio buttons for justification
			int sideBuffer = 10;
			this.justifyGroup = new System.Windows.Forms.GroupBox();
			this.mainGroup.Controls.Add(this.justifyGroup);
			this.justifyGroup.Text = "Justify to the: ";
			this.justifyGroup.Size = new System.Drawing.Size(this.mainGroup.Width - (sideBuffer * 2), 50);	// width, height
			this.justifyGroup.Location = new System.Drawing.Point(sideBuffer, sideBuffer);
			
			// Radio button for left justification, default
			this.leftJustify = new System.Windows.Forms.RadioButton();
			this.leftJustify.Text = "Left";
			this.leftJustify.Checked = true;
			this.justifyGroup.Controls.Add(this.leftJustify);
			this.leftJustify.Location = new System.Drawing.Point(5, 20);
			this.leftJustify.CheckedChanged += new System.EventHandler(justify_changeSelected);
			
			// Radio button for center justification
			this.centerJustify = new System.Windows.Forms.RadioButton();
			this.centerJustify.Text = "Center";
			this.centerJustify.Checked = false;
			this.justifyGroup.Controls.Add(this.centerJustify);
			this.centerJustify.Location = new System.Drawing.Point((this.justifyGroup.Width / 2 - 40), 20);
			this.centerJustify.CheckedChanged += new System.EventHandler(justify_changeSelected);
			
			// Radio button for right justification
			this.rightJustify = new System.Windows.Forms.RadioButton();
			this.rightJustify.Text = "Right";
			this.rightJustify.Checked = false;
			this.justifyGroup.Controls.Add(this.rightJustify);
			this.rightJustify.Location = new System.Drawing.Point(this.justifyGroup.Width - (this.justifyGroup.Width / 3) + 30, 20);
			this.rightJustify.CheckedChanged += new System.EventHandler(justify_changeSelected);
			this.rightJustify.Enabled = false;
			
			//// set flags //
			this.flag_justify = this.leftJustify.Text;
		}
		
		private System.Windows.Forms.Button button;
		private System.Windows.Forms.Label label;
		private System.Windows.Forms.TextBox labelPointer;
		private System.Windows.Forms.Button createText;
		private System.Windows.Forms.GroupBox mainGroup;
		private System.Windows.Forms.GroupBox justifyGroup;
		private System.Windows.Forms.RadioButton leftJustify;
		private System.Windows.Forms.RadioButton centerJustify;
		private System.Windows.Forms.RadioButton rightJustify;
		
		// flags
		private System.String flag_justify;
	}
}
