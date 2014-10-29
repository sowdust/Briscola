/*
 * Copyright (C) 2014 mat
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package briscola;

import jade.core.AID;

/**
 *
 * @author mat
 */
public class Player {

    AID agent;
    String name;

    public Player(AID agent, String name) {
        this.agent = agent;
        this.name = name;
    }

    public AID getAID() {
        return agent;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " [" + agent.getName() + "]";
    }
}
