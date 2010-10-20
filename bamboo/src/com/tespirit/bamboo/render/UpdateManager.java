package com.tespirit.bamboo.render;

import java.util.Collection;

public interface UpdateManager {
	public void addSingleUpdater(Updater updater);
	public void removeSingleUpdater(Updater updater);
	public void removeSingleUpdaters(Collection<Updater> updaters);

	public void addUpdater(Updater updater);
	public void removeUpdater(Updater updater);
	public void removeUpdaters(Collection<? extends Updater> updaters);

}
