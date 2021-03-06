importPackage(Packages.net.sf.odinms.server.maps);
importPackage(Packages.net.sf.odinms.net.channel);
importPackage(Packages.net.sf.odinms.tools);
importPackage(Packages.net.sf.odinms.client);

/*
Kerning PQ: 1st stage to 2nd stage portal
*/

function enter(pi) {
	var nextMap = 103000801;
	var eim = pi.getPlayer().getEventInstance()
	var target = eim.getMapInstance(nextMap);
	var targetPortal = target.getPortal("st00");
	// only let people through if the eim is ready
	var avail = eim.getProperty("1stageclear");
	if (avail == null || pi.getPlayer().isGM()) {
		// do nothing; send message to player
		pi.getPlayer().getClient().getSession().write(MaplePacketCreator.serverNotice(6, "The warp is currently unavailable."));
		return false;	}
	else {
		pi.getPlayer().changeMap(target, targetPortal);
		return true;
	}
}