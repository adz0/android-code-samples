package code.examples.ui;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityPlateModule {

    @ContributesAndroidInjector
    abstract ActivityPlate activityPlate();
}
