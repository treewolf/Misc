#include <gtk/gtk.h>
#include <stdlib.h>
#include <stdio.h>

/* Gets current volume and adjusts scale */
void initializeVolumeScale(GtkWidget * item){
	unsigned int preset = 50.0;
	gtk_range_set_value(GTK_RANGE (item), preset);
	system("amixer sset 'Master 50%");
	system("amixer sset 'Master' unmute");
	system("amixer sset 'PCM' 100%");
	system("amixer sset 'PCM' unmute");
	system("amixer sset 'Mic' 100%");
	system("amixer sset 'Mic' unmute");
}

/* Adjusts volume from volume scale. Uses unsigned integers 0 - 100 inclusive */
void adjustVolume(GtkWidget * item){
	char * cmd;
	cmd = (char *) calloc(30, sizeof (char *));
	sprintf(cmd, "amixer sset 'Master' %u%%", (unsigned int)gtk_range_get_value(GTK_RANGE (item)));
	system(cmd);
	free(cmd);
}

/* Mutes or unmutes volume */
void muteVolume(GtkWidget * item){
	if(gtk_toggle_button_get_active(GTK_TOGGLE_BUTTON (item)))
		system("amixer set 'Master' mute");
	else{
		system("amixer sset 'Master' unmute");
		system("amixer sset 'PCM' unmute");
	}
}

int main(int argc, char *argv[]){

	/* global variables to be reused */
	unsigned int uint = 0;  

	/* Declare all widgets here */
	GtkWidget * window;
	GtkWidget * notebook;
	GtkWidget * spacer;	/* this is a spacer for grid */
	GtkWidget * page_volume_label;
	GtkWidget * page_volume_grid;
	GtkWidget * page_volume_scale;
	GtkWidget * page_volume_scale_label;
	GtkWidget * page_volume_mute_toggle;

	gtk_init(&argc, &argv);

	/* Create a new window. This is the parent window */
    	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_title(GTK_WINDOW (window), "Control Panel");
	gtk_window_set_default_size(GTK_WINDOW (window), 200, 300);

	/* Create spacer */
	spacer = gtk_label_new(" ");

	/* Create a new notebook */
	notebook = gtk_notebook_new();
	gtk_container_add(GTK_CONTAINER (window), notebook);

	/* Page VOLUME config */
	/* label */
	page_volume_label = gtk_label_new("Volume Settings");
	/* volume scale */
	page_volume_scale = gtk_scale_new_with_range(GTK_ORIENTATION_VERTICAL,0,100,1);
	gtk_range_set_inverted(GTK_RANGE (page_volume_scale), TRUE);
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 0, GTK_POS_RIGHT, "0");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 25, GTK_POS_RIGHT, "25");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 50, GTK_POS_RIGHT, "50");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 75, GTK_POS_RIGHT, "75");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 100, GTK_POS_RIGHT, "100");
	gtk_scale_set_draw_value(GTK_SCALE (page_volume_scale), 1);
	initializeVolumeScale(page_volume_scale);
	/* scale label, Master */
	page_volume_scale_label = gtk_label_new("Master");
	/* checkbox for mute option */
	page_volume_mute_toggle = gtk_toggle_button_new_with_label("Muted");
	/* page grid for layout */
	page_volume_grid = gtk_grid_new();
	gtk_grid_attach(GTK_GRID (page_volume_grid), spacer, 0, 0, 5, 20);
	gtk_grid_attach(GTK_GRID (page_volume_grid), page_volume_scale_label, 4, 5, 10, 5);
	gtk_grid_attach(GTK_GRID (page_volume_grid), page_volume_scale, 5, 20, 10, 30);
	gtk_widget_set_hexpand(page_volume_scale, TRUE);
	gtk_widget_set_vexpand(page_volume_scale, TRUE);
	gtk_grid_attach(GTK_GRID (page_volume_grid), page_volume_mute_toggle, 7, 50, 4, 4);
	/* page */
	gtk_notebook_append_page(GTK_NOTEBOOK (notebook),page_volume_grid,page_volume_label);
	/* signal listener */
	g_signal_connect(page_volume_scale, "value-changed", G_CALLBACK (adjustVolume), page_volume_scale);
	g_signal_connect(page_volume_mute_toggle, "toggled", G_CALLBACK (muteVolume), page_volume_mute_toggle);
	gtk_widget_show_all(window);
	gtk_main ();    
	return 0;
}

