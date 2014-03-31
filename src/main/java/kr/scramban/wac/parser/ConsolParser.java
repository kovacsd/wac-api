package kr.scramban.wac.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.order.OrderParser;

public class ConsolParser {

    private final GameContext gameContext = new GameContext();

    private final Map<InputOrder, OrderParser> parsers = new HashMap<InputOrder, OrderParser>();

    {
        parsers.put(InputOrder.SETTINGS_OPPONENT_BOT, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                context.getPlayerList().addEnemy(args[0][0]);
                return null;
            }
        });
        parsers.put(InputOrder.SETTINGS_YOUR_BOT, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                context.getPlayerList().addMe(args[0][0]);
                return null;
            }
        });
        parsers.put(InputOrder.SETUP_MAP_SUPER_REGIONS, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                for (String[] superRegionArgs : args) {
                    context.getWorld().addSuperRegion(toInt(superRegionArgs[0]), toInt(superRegionArgs[0]));
                }
                return null;
            }
        });
        parsers.put(InputOrder.SETUP_MAP_REGIONS, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                for (String[] regionArgs : args) {
                    context.getWorld().addRegion(toInt(regionArgs[0]), toInt(regionArgs[0]));
                }
                return null;
            }
        });
        parsers.put(InputOrder.SETUP_MAP_NEIGHBORS, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                for (String[] regionArgs : args) {
                    context.getWorld().addNeighbors(toInt(regionArgs[0]), toInt(regionArgs[0].split(",")));
                }
                return null;
            }
        });
        parsers.put(InputOrder.PICK_STARTING_REGIONS, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                StringBuilder response = new StringBuilder();
                for (int i = 1; i < (args.length - 1) / 2; i++) {
                    if (response.length() > 0) {
                        response.append(" ");
                    }
                    response.append(args[i]);
                }
                return response.toString();
            }
        });
        parsers.put(InputOrder.SETTINGS_STARTING_ARMIES, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                context.getArmyCount().setReinforcement(toInt(args[0][0]));
                return null;
            }
        });
        parsers.put(InputOrder.UPDATE_MAP, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                for (String[] mapArgs : args) {
                    context.getWorld().setRegion(toInt(mapArgs[0]), context.getPlayerList().getPlayer(mapArgs[1]), toInt(mapArgs[2]));
                }
                return null;
            }
        });
        parsers.put(InputOrder.GO_PLACE_ARMIES, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                StringBuilder response = new StringBuilder();
                int reinforcement = context.getArmyCount().getReinforcement();
                List<Region> regions = context.getWorld().getMyRegions();
                String myName = context.getPlayerList().getMyName();
                while (reinforcement > 0) {
                    for (Region region : regions) {
                        if (!region.isHinterland()) {
                            if (response.length() > 0) {
                                response.append(",");
                            }
                            response.append(myName);
                            response.append(" place_armies ");
                            response.append(region.getId());
                            response.append(" 1");
                            reinforcement--;
                        }
                        if (reinforcement == 0) {
                            break;
                        }
                    }
                }
                return response.toString();
            }
        });
        parsers.put(InputOrder.GO_PLACE_ARMIES, new OrderParser() {
            @Override
            public String parse(final GameContext context, final String[][] args) {
                StringBuilder response = new StringBuilder();
                List<Region> regions = context.getWorld().getMyRegions();
                String myName = context.getPlayerList().getMyName();
                for (Region region : regions) {
                    if (region.isHinterland()) {
                        if (region.getArmy() > 1) {
                            for (Region neighbor : region.getNeighbor()) {
                                if (!neighbor.isHinterland()) {
                                    if (response.length() > 0) {
                                        response.append(",");
                                    }
                                    response.append(myName);
                                    response.append(" attack/transfer ");
                                    response.append(region.getId());
                                    response.append(" ");
                                    response.append(neighbor.getId());
                                    response.append(" ");
                                    response.append(region.getArmy() - 1);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (region.getArmy() > 1) {
                            for (Region neighbor : region.getNeighbor()) {
                                if (!neighbor.getOwner().isMe() && neighbor.getArmy() * 1.5 < region.getArmy() - 1) {
                                    if (response.length() > 0) {
                                        response.append(",");
                                    }
                                    response.append(myName);
                                    response.append(" attack/transfer ");
                                    response.append(region.getId());
                                    response.append(" ");
                                    response.append(neighbor.getId());
                                    response.append(" ");
                                    response.append(region.getArmy() - 1);
                                    break;
                                }
                            }
                        }
                    }
                }
                return response.toString();
            }
        });
    }

    private int toInt(final String arg) {
        return Integer.valueOf(arg).intValue();
    }

    private int[] toInt(final String[] args) {
        int[] result = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = toInt(args[i]);
        }
        return result;
    }

    public String parseLine(final String order) {
        String[] parts = order.split(" ");
        InputOrder inputOrder = InputOrder.getByOrderKeys(parts);
        OrderParser parser = parsers.get(inputOrder);
        return parser.parse(gameContext, inputOrder.paramParser(parts));
    }
}
