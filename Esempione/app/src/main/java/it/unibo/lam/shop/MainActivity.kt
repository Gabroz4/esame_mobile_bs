package it.unibo.lam.shop

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import it.unibo.lam.shop.ui.productList.ProductListFragment
import it.unibo.lam.shop.ui.productPage.ProductDetailsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Mostra il pulsante Back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) { // Importante per evitare di sovrapporre i fragment durante i riavvii dell'attività
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Gestisci il click sul pulsante Back
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}