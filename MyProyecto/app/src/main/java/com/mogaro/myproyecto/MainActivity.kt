// Monica Garcia Rodriguez

package com.mogaro.myproyecto
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.mogaro.myproyecto.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Estas dos líneas sustituyen a
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recogemos el componente ViewPager2
        val viewPager2 = binding.viewPager2

        // Se crea el adapter.
        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)

        // Se añaden los fragments y los títulos de pestañas.
        adapter.addFragment(Fragment_Principal(), "Principal")
        adapter.addFragment(Fragment_Historico(), "Historicos")

        // Se asocia el adpater al viewPager2
        viewPager2.adapter = adapter

        // Carga de las pestañas en el TabLayout
        TabLayoutMediator(binding.tabLayout, viewPager2){tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

    }

}

