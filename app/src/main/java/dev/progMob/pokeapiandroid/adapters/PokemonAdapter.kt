package dev.progMob.pokeapiandroid.adapters

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.databinding.ListItemPokemonBinding
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import dev.progMob.pokeapiandroidtask.utils.NETWORK_VIEW_TYPE
import dev.progMob.pokeapiandroidtask.utils.PRODUCT_VIEW_TYPE
import dev.progMob.pokeapiandroidtask.utils.getPicUrl

//Paging Adapter belonging to paging 3 in Android jetpack, used to paginate data.
class PokemonAdapter(private val navigate: (PokemonResult, Int, String?) -> Unit) :
    PagingDataAdapter<PokemonResult, PokemonAdapter.ViewHolder>(
        PokemonDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getItem(position)!!

        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ListItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class ViewHolder(
        private val binding: ListItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var dominantColor: Int = 0
        var picture: String? = ""

        fun bind(pokemonResult: PokemonResult) {
            binding.apply {
                pokemonItemTitle.text = pokemonResult.name.capitalize()
                loadImage(this, pokemonResult)

                root.setOnClickListener {
                    navigate.invoke(pokemonResult, dominantColor, picture)
                }
            }

        }

        private fun loadImage(binding: ListItemPokemonBinding, pokemonResult: PokemonResult) {

            /*Loading the image and hiding the progress bar after its done, also using palette library to extract dominant color
            and setting imageview background

             */

            picture = pokemonResult.url.getPicUrl()

            binding.apply {
                Glide.with(root)
                    .load(picture)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressCircular.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            val drawable = resource as BitmapDrawable
                            val bitmap = drawable.bitmap
                            Palette.Builder(bitmap).generate {
                                it?.let { palette ->
                                    dominantColor = palette.getDominantColor(
                                        ContextCompat.getColor(
                                            root.context,
                                            R.color.white
                                        )
                                    )

                                    pokemonItemImage.setBackgroundColor(dominantColor)


                                }
                            }
                            progressCircular.isVisible = false
                            return false
                        }

                    })
                    .into(pokemonItemImage)

            }
        }
    }

    internal class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonResult>() {
        override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem == newItem
        }
    }
    //checking if the pokemon are being displayed or the loading more progressbar inorder to set spans accordingly.

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            NETWORK_VIEW_TYPE
        } else {
            PRODUCT_VIEW_TYPE
        }
    }

}