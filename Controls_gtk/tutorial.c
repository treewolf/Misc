#include <gtk/gtk.h>
#include <stdlib.h>
#include <stdio.h>

/* Adjusts volume from volume scale. Uses unsigned integers 0 - 100 inclusive */
void adjustVolume(GtkWidget * item){
	char * cmd;
	cmd = (char *) calloc(30, sizeof (char *));
	sprintf(cmd, "amixer sset 'Master' %u%%", (unsigned int)gtk_range_get_value(GTK_RANGE (item)));
	system(cmd);
}

int main(int argc, char *argv[]){

	/* global variables to be reused */
	unsigned int uint = 0;  

	/* Declare all widgets here */
	GtkWidget * window;
	GtkWidget * notebook;
	GtkWidget * page_volume_label;
	GtkWidget * page_volume_table;
	GtkWidget * page_volume_scale;

	gtk_init(&argc, &argv);

	/* Create a new window. This is the parent window */
    	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_title(GTK_WINDOW (window), "Control Panel");
	gtk_window_resize(GTK_WINDOW (window), 200, 200);

	/* Create a new notebook */
	notebook = gtk_notebook_new();
	gtk_container_add(GTK_CONTAINER (window), notebook);

	/* Page VOLUME config */
	/* label */
	page_volume_label = gtk_label_new("Volume Settings");
	/* volume scale */
	page_volume_scale = gtk_scale_new_with_range(GTK_ORIENTATION_VERTICAL,0,100,1);
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 0, GTK_POS_RIGHT, "0");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 25, GTK_POS_RIGHT, "25");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 50, GTK_POS_RIGHT, "50");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 75, GTK_POS_RIGHT, "75");
	gtk_scale_add_mark(GTK_SCALE (page_volume_scale), 100, GTK_POS_RIGHT, "100");
	gtk_scale_set_draw_value(GTK_SCALE (page_volume_scale), 1);
	/* page */
	gtk_notebook_append_page(GTK_NOTEBOOK (notebook),page_volume_scale,page_volume_label);
	/* signal listener */
	g_signal_connect(page_volume_scale, "value-changed", G_CALLBACK (adjustVolume), page_volume_scale);

	gtk_widget_show_all(window);
	gtk_main ();    
	return 0;
}

