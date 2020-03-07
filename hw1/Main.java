/*
 * Author: Jacob Freedman
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	public static ArrayList<variable> vars = new ArrayList<variable>();

	public static void main(String[] args) {
		try {
			long start = System.nanoTime();
			int lineCount = 1;
			//File prog = new File(args[0]);
			File prog = new File("test");
			Scanner fileLine = new Scanner(prog);
			while (fileLine.hasNextLine()) {
				String line = fileLine.nextLine();

				interpret(line, lineCount);
				lineCount++;
			}
			fileLine.close();
			// fileIn.close();
			long end = 	 System.nanoTime();
			System.out.println( "run time: "+ (end-start)/1000000000.0);
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
		}
	}

	public static void interpret(String s, int lineCount) {
		String[] breakdown = s.split(" ");

		if (breakdown[0].compareToIgnoreCase("for") == 0) {
			int loop = Integer.parseInt(breakdown[1]);
			String newLine = "";
			for (int len = 2; len < breakdown.length; len++) {
				if(breakdown[len].equals(" ") || breakdown[len].equalsIgnoreCase("ENDFOR") || breakdown[len].equalsIgnoreCase("") ) {
					
				} else {
				newLine += breakdown[len] + " ";
				}
			}
			String[] newBreakdown = newLine.split(";");

			for (int j = 0; j < newBreakdown.length; j++) {
				newBreakdown[j] = newBreakdown[j] + ";";
				if (newBreakdown[j].charAt(0) == ' ') {
					newBreakdown[j] = newBreakdown[j].trim();
				}
			}
			
			int x = 0;
			for (int i = 0; i < loop; i++) {
				
				for (x = 0; x < newBreakdown.length; x++) {
					//System.out.println(newBreakdown[x]);
					interpret(newBreakdown[x], lineCount);
				}
				
				x = 0;
			}
			
		}

		if (breakdown[0].compareToIgnoreCase("print") == 0) {

			String temp = breakdown[1].charAt(0) + "";
			if (temp.contentEquals("\"")) {
				String printable = breakdown[1].replace("\"", "");
				System.out.println(printable);

			} else {
				int count = 0;
				for (variable i : vars) {
					count++;
					if (i.getName().equals(breakdown[1].toLowerCase())) {
						if (i.getVal().equals("")) {
							System.out.println("Runtime Error: Line " + lineCount);
							System.exit(0);
						}
						i.setVal(i.getVal().replace("\"", ""));
						System.out.println(i.getVal());

					}

				}
				if (count == vars.size() + 1) {
					System.out.println("Runtime Error: Line " + lineCount);
					System.exit(0);
				}
			}
		} else if (breakdown[0].compareToIgnoreCase("int") == 0) {

			String intName = breakdown[1];
			boolean isValid = false;
			if (breakdown.length == 3) {

				vars.add(new variable(intName.toLowerCase()));
				vars.get(vars.size() - 1).type = "int";
			} else {
				try {
					if (breakdown[3].equals("TRUE") || breakdown[3].equals("FALSE")) {
						isValid = true;
					} else {
						int testInt = Integer.parseInt(breakdown[3]);
					}
				} catch (Exception e) {
					System.out.println("Runtime Error: Line " + lineCount);
					System.exit(0);
				}
				if (isValid) {
					vars.add(new variable(intName.toLowerCase()));
					vars.get(vars.size() - 1).type = "bool";
					if (breakdown[3].contentEquals("TRUE")) {
						vars.get(vars.size() - 1).setVal("0");
					} else {
						vars.get(vars.size() - 1).setVal("1");
					}
				} else {
					vars.add(new variable(intName.toLowerCase()));
					vars.get(vars.size() - 1).type = "int";
					if (breakdown.length > 3) {
						vars.get(vars.size() - 1).setVal(breakdown[3]);
					}
				}
			}
		}

		else if (breakdown[0].compareToIgnoreCase("bool") == 0) {
			String boolName = breakdown[1];

			if (breakdown.length == 3) {
				vars.add(new variable(boolName.toLowerCase()));
				vars.get(vars.size() - 1).type = "bool";
			} else {

				if (breakdown[3].equals("TRUE") || breakdown[3].equals("FALSE")) {
					vars.add(new variable(boolName.toLowerCase()));
					vars.get(vars.size() - 1).type = "bool";
					if (breakdown.length > 3) {
						vars.get(vars.size()).value = breakdown[3];
					}
				} else {
					if (breakdown[3].equalsIgnoreCase("TRUE")) {
						System.out.println("Runtime Error: Line " + lineCount);
						System.exit(0);
					} else if (breakdown[3].equalsIgnoreCase("FALSE")) {
						System.out.println("Runtime Error: Line " + lineCount);
						System.exit(0);
					} else {
						System.out.println("Runtime Error: Line " + lineCount);
						System.exit(0);
					}
					System.exit(0);
				}
			}
		} else if (breakdown[0].compareToIgnoreCase("string") == 0) {
			String sName = breakdown[1];

			if (breakdown.length == 3) {
				vars.add(new variable(sName.toLowerCase()));
				vars.get(vars.size() - 1).type = "string";
			} else {
				String start = breakdown[3].charAt(0) + "";
				String end = breakdown[3].charAt(breakdown[3].length() - 1) + "";
				if (start.equals("\"") && end.equals("\"")) {
					vars.add(new variable(sName.toLowerCase()));
					vars.get(vars.size() - 1).type = "string";
					if (breakdown.length > 3) {
						vars.get(vars.size()).value = breakdown[3];
					}
				} else {
					System.out.println("Runtime Error: Line " + lineCount);
					System.exit(0);
				}
			}
		} else {
			String varName = breakdown[0].toLowerCase();
			for (variable name : vars) {

				if (name.getName().equals(varName)) {
					if (name.getInit() == false) {
						name.setVal(breakdown[2]);
						name.init = true;
					} else {

						String start = breakdown[2].charAt(0) + "";
						String end = breakdown[2].charAt(breakdown[2].length() - 1) + "";
						if (breakdown[1].equals("=")) {
							if (tryInt(breakdown[2])) {
								name.setVal(breakdown[2]);
							} else if (breakdown[2].equals("TRUE") || breakdown[2].equals("FALSE")) {
								name.setVal(breakdown[2]);
							}

							else if (start.equals("\"") && end.equals("\"")) {
								name.setVal(breakdown[2]);
							}
						} else if (breakdown[1].equals("+=")) {

							if (name.getType().equals("string")) {
								String ch = breakdown[2].charAt(0) + "";
								if (ch.equals("\"")) {
									name.setVal(name.getVal() + breakdown[2]);
								} else {
									for (variable i : vars) {
										if (breakdown[2].equals(i.getName())) {
											name.setVal(name.getVal() + i.getVal());
										}
									}
								}
							}
							if (name.getType().equals("int") && tryInt(breakdown[2])) {

								int tempIntVal = Integer.parseInt(name.getVal()) + Integer.parseInt(breakdown[2]);
								name.setVal("" + tempIntVal);
							} else {
								String temp = breakdown[2].charAt(0) + "";
								if (temp.equals("\"") && name.getType().equals("int")) {
									System.out.println("Runtime Error: Line " + lineCount);
									System.exit(0);
								}
								if (breakdown[2].equals("TRUE")) {
									int x = Integer.parseInt(name.getVal());
									name.setVal(x + 1 + "");
								} else {
									int count = 0;
									for (variable second : vars) {
										count++;
										if (breakdown[2].toLowerCase().equals(second.getName())) {
											if (second.getVal().equals("")) {
												System.out.println("Runtime Error: Line " + lineCount);
												System.exit(0);
											}
											if (second.getInit() && second.getType().equals("int")) {
												int x = Integer.parseInt(name.getVal())
														+ Integer.parseInt(second.getVal());
												name.setVal(x + "");
											} else if (second.getType().equals("bool")) {
												if (second.getVal().equals("TRUE")) {
													int x = Integer.parseInt(name.getVal());
													name.setVal(x + 1 + "");
												}
											} else {
												System.out.println("Runtime Error: Line " + lineCount);
												System.exit(0);
											}
										}
									}

									if (count == vars.size() + 1) {
										System.out.println("Runtime Error: Line " + lineCount);
										System.exit(0);
									}
								}
							}
						} else if (breakdown[1].equals("*=")) {
							if (name.getType().equals("int") && tryInt(breakdown[2])) {

								int tempIntVal = Integer.parseInt(name.getVal()) * Integer.parseInt(breakdown[2]);
								name.setVal("" + tempIntVal);
							} else {
								String temp = breakdown[2].charAt(0) + "";
								if (temp.equals("\"") && name.getType().equals("int")) {
									System.out.println("Runtime Error: Line " + lineCount);
									System.exit(0);
								}
								int count = 0;
								for (variable second : vars) {
									count++;
									if (breakdown[2].toLowerCase().equals(second.getName())) {
										if (breakdown[2].toLowerCase().equals(second.getName())) {
											if (second.getVal().equals("")) {
												System.out.println("Runtime Error: Line " + lineCount);
												System.exit(0);
											}
											if (second.getInit() && second.getType().equals("int")) {
												int x = Integer.parseInt(name.getVal())
														* Integer.parseInt(second.getVal());
												name.setVal(x + "");
											} else {
												System.out.println("Runtime Error: Line " + lineCount);
												System.exit(0);
											}
										}

										if (count == vars.size() + 1) {
											System.out.println("Runtime Error: Line " + lineCount);
											System.exit(0);
										}
									}
								}
							}
						} else if (breakdown[1].equals("-=")) {
							if (name.getType().equals("int") && tryInt(breakdown[2])) {

								int tempIntVal = Integer.parseInt(name.getVal()) - Integer.parseInt(breakdown[2]);
								name.setVal("" + tempIntVal);
							} else {
								String temp = breakdown[2].charAt(0) + "";
								if (temp.equals("\"") && name.getType().equals("int")) {
									System.out.println("Runtime Error: Line " + lineCount);
									System.exit(0);
								}
								if (breakdown[2].equals("TRUE")) {
									int x = Integer.parseInt(name.getVal());
									name.setVal(x - 1 + "");
								} else {
									int count = 0;
									for (variable second : vars) {
										if (breakdown[2].toLowerCase().equals(second.getName())) {
											if (second.getVal().equals("")) {
												System.out.println("Runtime Error: Line " + lineCount);
												System.exit(0);
											}
										}
										count++;
										if (breakdown[2].toLowerCase().equals(second.getName())) {
											if (second.getInit() && second.getType().equals("int")) {
												int x = Integer.parseInt(name.getVal())
														- Integer.parseInt(second.getVal());
												name.setVal(x + "");
											} else if (second.getType().equals("bool")) {
												if (second.getVal().equals("TRUE")) {
													int x = Integer.parseInt(name.getVal());
													name.setVal(x - 1 + "");
												}
											} else {
												System.out.println("Runtime Error: Line " + lineCount);
												System.exit(0);
											}
										}
									}

									if (count == vars.size() + 1) {
										System.out.println("Runtime Error: Line " + lineCount);
										System.exit(0);
									}
								}
							}
						}
					}

				}

			}

		}

	}

	static boolean tryInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
