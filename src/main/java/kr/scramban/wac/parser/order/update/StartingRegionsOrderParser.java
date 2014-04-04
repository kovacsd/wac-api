package kr.scramban.wac.parser.order.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.domain.map.SuperRegion;
import kr.scramban.wac.parser.order.OrderParser;

public class StartingRegionsOrderParser implements OrderParser {

    private final static Comparator<SuperRegion> c = new Comparator<SuperRegion>() {

        @Override
        public int compare(final SuperRegion superRegion1, final SuperRegion superRegion2) {
            return superRegion1.getRegionsCount() - superRegion2.getRegionsCount();
        }
    };

    private final GameContext context;

    public StartingRegionsOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        StringBuilder response = new StringBuilder();
        List<? extends SuperRegion> superRegions = getSuperRegionBySize();
        int selectableStartingRegion = (args[0].length - 1) / 2;
        for (SuperRegion superRegion : superRegions) {
            for (String regionId : args[0]) {
                if (selectableStartingRegion > 0) {
                    Region region = context.getWorld().getRegion(Integer.parseInt(regionId));
                    if (superRegion.contains(region)) {
                        if (response.length() > 0) {
                            response.append(" ");
                        }
                        response.append(region.getId());
                        selectableStartingRegion--;
                    }
                }
            }
        }
        return response.toString();
    }

    private List<? extends SuperRegion> getSuperRegionBySize() {
        List<? extends SuperRegion> superRegion = new ArrayList<SuperRegion>(context.getWorld().getSuperRegions());
        Collections.sort(superRegion, c);
        return superRegion;
    }
}