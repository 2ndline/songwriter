package com.secondline.songwriter;

import java.io.FileNotFoundException;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.secondline.songwriter.model.Lyric;
import com.secondline.songwriter.model.Song;

public class Songwriter {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		Options options = loadOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("songwriter", options);
			return;
		}

		if (cmd.hasOption("a")) {
			// get the file & analyze
			String filename = cmd.getOptionValue("a");
			try {
				Song song = LyricsUtil.getSongFromLyricsFile(filename);
				String output = song == null ? "No lyrics detected" : song.toString();
				System.out.println(output);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	private static Options loadOptions() {
		Options options = new Options();
		options.addOption(new Option("h", "help", false, "This message"));
		options.addOption(Option.builder("a").hasArg().argName("file")
				.desc("use given file for analysis").build());
		return options;
	}

	
}
