function enter(pi) {
	pi.warp(230040000, 0);
	pi.getPlayer().getClient().getSession().write(Packages.net.sf.odinms.tools.MaplePacketCreator.musicChange("Bgm12/AquaCave"));
	return true;
}