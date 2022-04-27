package com.hyunshop.seeder;

import java.util.Arrays;

public class BatchSeeder implements Seeder {

    private final Seeder[] seeders = new Seeder[]{
            new RoleSeeder(),
            new UserSeeder(),
            new CategorySeeder(),
            new ProductSeeder()
    };

    @Override
    public void seed() {
        Arrays.stream(seeders).forEach(Seeder::seed);
    }
}
