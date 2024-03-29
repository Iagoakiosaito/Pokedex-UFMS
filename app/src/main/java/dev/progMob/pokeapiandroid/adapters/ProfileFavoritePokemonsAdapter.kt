package dev.progMob.pokeapiandroid.adapters

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroid.databinding.ListItemPokemonBinding
import dev.progMob.pokeapiandroid.model.PokemonResult
import dev.progMob.pokeapiandroid.utils.getPicUrl


class ProfileFavoritePokemonsAdapter(private val favoritePokemon: List<FavoritePokemon>) :
    RecyclerView.Adapter<ProfileFavoritePokemonsAdapter.FavoritePokemonViewHolder>(){

    var onItemClick: ((favoritePokemon: FavoritePokemon) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePokemonViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_item_pokemon, parent, false)
//
//        return FavoritePokemonViewHolder(view)
        return FavoritePokemonViewHolder(
            ListItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritePokemonViewHolder, position: Int) {
        if(favoritePokemon.isNotEmpty()){
            holder.bindView(favoritePokemon[position])
        }
    }

    override fun getItemCount(): Int {
        return favoritePokemon.size
    }


    inner class FavoritePokemonViewHolder(
        private val binding: ListItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var dominantColor: Int = 0
        var picture: String? = ""

        fun bindView(favoritePokemon: FavoritePokemon){
            binding.apply {
                pokemonItemTitle.text = favoritePokemon.pokemonResult.name
                loadImage(binding, favoritePokemon.pokemonResult)

                root.setOnClickListener {
                    onItemClick?.invoke(favoritePokemon)
                }
            }
        }

        private fun loadImage(binding: ListItemPokemonBinding, pokemonResult: PokemonResult) {

            /*
            Loading the image and hiding the progress bar after its done, also using palette library to extract dominant color
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


}