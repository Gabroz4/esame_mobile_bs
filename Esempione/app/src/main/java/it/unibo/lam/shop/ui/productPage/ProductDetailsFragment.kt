package it.unibo.lam.shop.ui.productPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import it.unibo.lam.shop.R
import it.unibo.lam.shop.data.product.Product
import it.unibo.lam.shop.data.product.local.ProductRepository


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "productId"

class ProductDetailsFragment : Fragment() {

    private var productId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_product_details, container, false)

        this.productId?.let {
            val product = ProductRepository.getProduct(it)
            if (product != null) {
                popolateView(product, view)
            }
        }

        return view
    }

    fun popolateView(product: Product, view: View) {
        val titleText =  view.findViewById<TextView>(R.id.product_title_text)
        val descriptionText =  view.findViewById<TextView>(R.id.product_description_text)
        val categoryText =  view.findViewById<TextView>(R.id.product_category_text)
        val priceText =  view.findViewById<TextView>(R.id.product_price_text)
        val imageView =  view.findViewById<ImageView>(R.id.product_image)
        titleText.text = product.title
        descriptionText.text = product.description
        priceText.text = product.price.toString()
        categoryText.text = product.category.name
        setImageOnView(product.images.get(0), imageView)
    }


    fun setImageOnView(resourceName: String, imageView: ImageView) {
        val resourceId = resources.getIdentifier(resourceName, "drawable", "it.unibo.lam.shop")
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            // La risorsa specificata non esiste
            // Gestire questa situazione in modo appropriato
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param productId ID del prodotto
         * @return A new instance of fragment ProductDetails.
         */
        fun newInstance(productId: Int) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, productId)
                }
            }
    }
}
