/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.odinms.net.channel.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.AbstractMaplePacketHandler;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.data.input.SeekableLittleEndianAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matze
 */
public class TrockAddMapHandler extends AbstractMaplePacketHandler {

    private static Logger log = LoggerFactory.getLogger(TrockAddMapHandler.class);

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        Connection con = DatabaseConnection.getConnection();
        byte addrem = slea.readByte();
        slea.readByte();
        if (addrem == 0x03) { //delete
            int mapId = slea.readInt();
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM trocklocations WHERE characterid = ? AND mapid = ?");
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, mapId);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException se) {
                log.error("SQL error: " + se.getLocalizedMessage(), se);
            }
            c.getSession().write(MaplePacketCreator.TrockRefreshMapList(c.getPlayer().getId(), true));
        } else if (addrem == 0x01) {
            try {
                PreparedStatement ps = con.prepareStatement("insert into trocklocations (characterid, mapid) VALUES (?, ?)");
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, c.getPlayer().getMapId());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException se) {
                log.error("SQL error: " + se.getLocalizedMessage(), se);
            }
            c.getSession().write(MaplePacketCreator.TrockRefreshMapList(c.getPlayer().getId(), true));
        }
    }
}