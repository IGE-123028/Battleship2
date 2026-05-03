package battleship;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The type Tasks.
 */
public class Tasks {
	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LogManager.getLogger(Tasks.class);

	/**
	 * The constant GOODBYE_MESSAGE.
	 */
	private static final String GOODBYE_MESSAGE = "Bons ventos!";

	/**
	 * Strings to be used by the user
	 */
	private static final String AJUDA = "ajuda";
	private static final String GERAFROTA = "gerafrota";
	private static final String LEFROTA = "lefrota";
	private static final String DESISTIR = "desisto";
	private static final String RAJADA = "rajada";
	private static final String TIROS = "tiros";
	private static final String MAPA = "mapa";
	private static final String STATUS = "estado";
	private static final String SIMULA = "simula";
	private static final String PDF = "pdf";
	private static final String IDIOMA = "idioma";
	private static final String IA = "ia";

	/**
	 * This task also tests the fighting element of a round of three shots
	 */
	public static void menu() {

		IFleet myFleet = null;
		IGame game = null;
		menuHelp();

		System.out.print("> ");
		Scanner in = new Scanner(System.in);
		String command = in.next();
		while (!command.equals(DESISTIR)) {

			switch (command) {
				case GERAFROTA:
					myFleet = Fleet.createRandom();
					game = new Game(myFleet);
					game.printMyBoard(false, true);
					break;
				case IDIOMA:
					String lang = in.next();

					try {
						Messages.load(lang);
						System.out.println("Linguagem alterada para: " + lang);
						menuHelp();
					} catch (Exception e) {
						System.out.println("Erro ao mudar linguagem.");
					}
					break;
				case LEFROTA:
					myFleet = buildFleet(in);
					game = new Game(myFleet);
					game.printMyBoard(false, true);
					break;
				case STATUS:
					if (myFleet != null)
						myFleet.printStatus();
					break;
				case MAPA:
					if (myFleet != null)
						game.printMyBoard(false, true);
					break;
				case RAJADA:
					if (game != null) {
						game.readEnemyFire(in);
						myFleet.printStatus();
						game.printMyBoard(true, false);

						if (game.getRemainingShips() == 0) {
							game.over();
							System.exit(0);
						}
					}
					break;
				case SIMULA:
					if (game != null) {
						while (game.getRemainingShips() > 0) {
							game.randomEnemyFire();
							myFleet.printStatus();
							game.printMyBoard(true, false);
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt(); // Best practice: restore interrupt status
							}
						}

						if (game.getRemainingShips() == 0) {
							game.over();
							System.exit(0);
						}
					}
					break;
				case TIROS:
					if (game != null)
						game.printMyBoard(true, true);
					break;
				case PDF:
					if (game != null) {
						Scanner scanner = new Scanner(System.in);
						System.out.print(
								"Enter the path where you want to save the PDF (do not need to include the *.pdf extension): ");
						String fileName = scanner.nextLine();
						PDFExporter.exportGameToPDF(game, fileName + ".pdf");
					} else {
						System.out.println("No game in progress.");
					}
					break;
				case IA:
					if (game != null) {
						System.out.println("A preparar para interagir com o LLM (Hugging Face)...");
						String token = "";
						java.io.Console console = System.console();
						if (console != null) {
							char[] passwordChars = console.readPassword(
									"Introduza o seu Token da Hugging Face (o input ficara oculto por seguranca): ");
							if (passwordChars != null) {
								token = new String(passwordChars).trim();
							}
						} else {
							// Fallback para ambientes sem consola interativa (ex: IDEs)
							System.out.print("Introduza o seu Token da Hugging Face: ");
							String tokenInput = in.nextLine();
							if (tokenInput.trim().isEmpty()) {
								tokenInput = in.nextLine();
							}
							token = tokenInput.trim();
						}

						if (token.isEmpty() || token.length() < 10) {
							System.out.println("Erro: Token da Hugging Face inválido ou não introduzido.");
							break;
						}
						LLMService llmService = new LLMService(token, LLMService.DEFAULT_MODEL);
						System.out.println("A preparar para interagir com o LLM (Hugging Face)...");
						try {
						boolean quitIA = false;
						while (game.getRemainingShips() > 0 && game.getRemainingAlienShips() > 0 && !quitIA) {
							boolean inputValido = false;
							List<IPosition> humanShots = new ArrayList<>();
							while (!inputValido) {
								System.out.println("\nA sua vez de atirar! Introduza " + Game.NUMBER_SHOTS
										+ " posições (ex: A1 B2 C3),\nou um comando (ex: /mapa, /estado, /pdf, /ajuda) ou 'q' para desistir:");
								humanShots.clear();
								String inputLine = in.nextLine();
								if (inputLine.trim().isEmpty()) {
									inputLine = in.nextLine();
								}
								inputLine = inputLine.trim();

								if (inputLine.equalsIgnoreCase("q") || inputLine.equalsIgnoreCase("/desisto")) {
									System.out.println("Desistiu do jogo contra a IA.");
									Scoreboard.saveResult("Jogo contra IA terminado por desistência");
									quitIA = true;
									break;
								}

								if (inputLine.startsWith("/")) {
									String cmd = inputLine.substring(1).toLowerCase();
									switch (cmd) {
										case MAPA:
											game.printMyBoard(false, true);
											break;
										case STATUS:
											myFleet.printStatus();
											break;
										case TIROS:
											game.printMyBoard(true, true);
											break;
										case PDF:
											System.out.print(
													"Enter the path where you want to save the PDF (do not need to include the *.pdf extension): ");
											String fileName = in.nextLine();
											PDFExporter.exportGameToPDF(game, fileName + ".pdf");
											break;
										case AJUDA:
											System.out.println("Comandos disponíveis durante o jogo contra IA:");
											System.out.println("- /" + MAPA + ": " + Messages.get("help.mapa"));
											System.out.println("- /" + STATUS + ": " + Messages.get("help.estado"));
											System.out.println("- /" + TIROS + ": " + Messages.get("help.tiros"));
											System.out.println("- /" + PDF + ": " + Messages.get("help.pdf"));
											System.out.println("- /" + DESISTIR + " ou 'q': " + Messages.get("help.desisto"));
											break;
										default:
											System.out.println("Comando desconhecido: /" + cmd);
									}
									continue;
								}

								try {
									Scanner lineScanner = new Scanner(inputLine);
									while (humanShots.size() < Game.NUMBER_SHOTS && lineScanner.hasNext()) {
										String shotToken = lineScanner.next();
										if (shotToken.equalsIgnoreCase("rajada")) {
											continue;
										}
										if (shotToken.matches("[A-Za-z]")) {
											if (lineScanner.hasNextInt()) {
												int row = lineScanner.nextInt();
												humanShots
														.add(new Position(shotToken.toUpperCase().charAt(0), row));
											}
										} else {
											Scanner singleScanner = new Scanner(shotToken);
											humanShots.add(Tasks.readClassicPosition(singleScanner));
										}
									}

									if (humanShots.size() != Game.NUMBER_SHOTS) {
										System.out.println("Entrada incompleta. Introduza as " + Game.NUMBER_SHOTS
												+ " posições ou um comando.");
									} else {
										inputValido = true;
									}
								} catch (Exception ex) {
									System.out.println(
											"Formato de posições inválido. Tente novamente apenas com as posições (ex: A1 B2 C3) ou um comando.");
								}
							}

							if (quitIA) break;

							game.fireMyShots(humanShots);
							System.out.println("Tiros efetuados na frota inimiga:");
							game.printAlienBoard(true, false);

							if (game.getRemainingAlienShips() == 0) {
								System.out.println("\nParabéns! Destruiu a frota inimiga!");
								game.over();
								break;
							}

							System.out.println("\nA perguntar ao LLM o próximo movimento...");
							String jsonShots = llmService.getNextMove((Game) game);
							System.out.println("LLM propõe: " + jsonShots);

							List<IPosition> shots = parseJsonShots(jsonShots);
							game.fireShots(shots);

							System.out.println("A sua frota após o ataque do LLM:");
							myFleet.printStatus();
							game.printMyBoard(true, false);

							if (game.getRemainingShips() == 0) {
								System.out.println("\nA IA destruiu a sua frota!");
								game.over();
								break;
							}
							Thread.sleep(2000);
						}
						} catch (Exception e) {
							System.out.println("Erro na interação com o LLM: " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						System.out.println("Gere uma frota primeiro!");
					}
					break;
				case AJUDA:
					menuHelp();
					break;
				default:
					System.out.println("Que comando é esse??? Repete ...");
			}
			System.out.print("> ");
			command = in.next();
		}
		System.out.println(GOODBYE_MESSAGE);
		Scoreboard.saveResult("Jogo terminado por desistência");
	}

	/**
	 * This function provides help information about the menu commands.
	 */
	public static void menuHelp() {
		System.out.println("======================= " + Messages.get("help.title") + " =========================");
		System.out.println("- " + GERAFROTA + ": " + Messages.get("help.gerafrota"));
		System.out.println("- " + LEFROTA + ": " + Messages.get("help.lefrota"));
		System.out.println("- " + STATUS + ": " + Messages.get("help.estado"));
		System.out.println("- " + MAPA + ": " + Messages.get("help.mapa"));
		System.out.println("- " + RAJADA + ": " + Messages.get("help.rajada"));
		System.out.println("- " + SIMULA + ": " + Messages.get("help.simula"));
		System.out.println("- " + TIROS + ": " + Messages.get("help.tiros"));
		System.out.println("- " + DESISTIR + ": " + Messages.get("help.desisto"));
		System.out.println("- " + PDF + ": " + Messages.get("help.pdf"));
		System.out.println("- " + IA + ": " + Messages.get("help.ia"));
		System.out.println("- " + IDIOMA + ": " + Messages.get("help.idioma"));
		System.out.println("===============================================================");
	}

	/**
	 * This operation allows the build up of a fleet, given user data
	 *
	 * @param in The scanner to read from
	 * @return The fleet that has been built
	 */
	public static Fleet buildFleet(Scanner in) {

		assert in != null;

		Fleet fleet = new Fleet();
		int i = 0; // i represents the total of successfully created ships
		while (i < Fleet.FLEET_SIZE) {
			IShip s = readShip(in);
			if (s != null) {
				boolean success = fleet.addShip(s);
				if (success)
					i++;
				else
					LOGGER.info("Falha na criacao de {} {} {}", s.getCategory(), s.getBearing(), s.getPosition());
			} else {
				LOGGER.info("Navio desconhecido!");
			}
		}
		LOGGER.info("{} navios adicionados com sucesso!", i);
		return fleet;
	}

	/**
	 * This operation reads data about a ship, build it and returns it
	 *
	 * @param in The scanner to read from
	 * @return The created ship based on the data that has been read
	 */
	public static Ship readShip(Scanner in) {

		assert in != null;

		String shipKind = in.next();
		Position pos = readPosition(in);
		char c = in.next().charAt(0);
		Compass bearing = Compass.charToCompass(c);
		return Ship.buildShip(ShipType.fromCode(shipKind), bearing, pos);
	}

	/**
	 * This operation allows reading a position in the map
	 *
	 * @param in The scanner to read from
	 * @return The position that has been read
	 */
	public static Position readPosition(Scanner in) {

		assert in != null;

		int row = in.nextInt();
		int column = in.nextInt();
		return new Position(row, column);
	}

	/**
	 * This operation allows reading a position in the map
	 *
	 * @param in The scanner to read from
	 * @return The classic position that has been read
	 */
	public static IPosition readClassicPosition(@NotNull Scanner in) {
		// Verifica se ainda há tokens disponíveis
		if (!in.hasNext()) {
			throw new IllegalArgumentException("Nenhuma posição válida encontrada!");
		}

		String part1 = in.next(); // Primeiro token
		String part2 = null;

		if (in.hasNextInt()) {
			part2 = in.next(); // Segundo token, se disponível
		}

		String input = (part2 != null) ? part1 + part2 : part1;

		// Normalizar o input para tratar letras maiúsculas e minúsculas
		input = input.toUpperCase();

		// Verificar os dois formatos possíveis: compactos e com espaço
		if (input.matches("[A-Z]\\d+")) {
			char column = input.charAt(0); // Extrair a coluna
			int row = Integer.parseInt(input.substring(1)); // Extrair a linha
			return new Position(column, row);
		} else if (part2 != null && part1.matches("[A-Z]") && part2.matches("\\d+")) {
			char column = part1.charAt(0); // Extrair a coluna
			int row = Integer.parseInt(part2); // Extrair a linha
			return new Position(column, row);
		} else {
			throw new IllegalArgumentException("Formato inválido. Use 'A3', 'A 3' ou similar.");
		}
	}

	/**
	 * Parses a JSON string representing a list of shots into a list of IPosition
	 * objects.
	 *
	 * @param json the JSON string containing the shots.
	 * @return a list of IPosition objects.
	 * @throws IOException if there is an error during JSON parsing.
	 */
	private static List<IPosition> parseJsonShots(String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> shotsData = objectMapper.readValue(json,
				new TypeReference<List<Map<String, Object>>>() {
				});
		List<IPosition> shots = new ArrayList<>();
		for (Map<String, Object> shotMap : shotsData) {
			String rowStr = (String) shotMap.get("row");
			char row = rowStr.charAt(0);
			int column = (int) shotMap.get("column");
			shots.add(new Position(row, column));
		}
		return shots;
	}

}
