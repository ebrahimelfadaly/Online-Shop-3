package com.example.onlineshop.ui.ShopTap.shopTapcategory

import androidx.fragment.app.Fragment
import kotlinx.coroutines.*


@DelicateCoroutinesApi
class KidsProductFragment : Fragment() {
//   lateinit var shopViewModel: ShopViewModel
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val application = requireNotNull(this.activity).application
//        val repository = RepositoryImpl(
//            RemoteDataSourceImpl(),
//            RoomDataSourceImpl(RoomService.getInstance(application)))
//     val viewModelFactory=ViewModelFactory(repository,application)
//         shopViewModel=ViewModelProvider(this,viewModelFactory).get(ShopViewModel::class.java)
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_kids_product, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val shf1 = requireActivity().findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout1)
//        val shf2 = requireActivity().findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout2)
//
//        shf1.startShimmer()
//        shf2.startShimmer()
//         if (NetworkChange.isOnline)
//         {
//               networkKidsView.visibility=View.GONE
//             kids_lin.visibility = View.VISIBLE
//             shopViewModel.fetchKidsProductsList().observe(viewLifecycleOwner,{
//                 if (it != null){
//                     shimmerFrameLayout1.stopShimmer()
//                     shimmerFrameLayout2.stopShimmer()
//                     shimmerFrameLayout1.visibility = View.GONE
//                     shimmerFrameLayout2.visibility = View.GONE
//
//                     itemsRecView.visibility = View.VISIBLE
//                     bindWomanProductRecyclerView(it.products,shopViewModel.intentTOProductDetails)
//                     Log.i("output",it.products.get(0).toString())
//                 }
//             })
//             shopViewModel.intentTOProductDetails.observe(requireActivity(),{
//                 if (NetworkChange.isOnline){
//                     shopViewModel.intentTOProductDetails= MutableLiveData()
////                     val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id.toLong())
////                     findNavController().navigate(action)
//                 }else{
//                     Toast.makeText(requireContext(),requireContext().resources.getString(R.string.no_internet_connection),
//                         Toast.LENGTH_SHORT).show()
//                 }
//
//             })
//             shopViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, {
//                 val allCodes = it
//                 if (allCodes != null) {
//                     play.setOnClickListener {
//                         play.visibility = View.GONE
//                         Glide.with(this)
//                             .load(R.drawable.kids_gif)
//                             .into(ads)
//                         GlobalScope.launch(Dispatchers.Main) { delay(1500)
//                             lin.visibility = View.VISIBLE
//                             codeTextView.text = allCodes.discountCodes[1].code}
//                         Log.i("output",allCodes.discountCodes[1].code)
//                     }
//
//                 }
//             })}
//
//        else{
//             networkKidsView.visibility = View.VISIBLE
//             kids_lin.visibility = View.GONE
//        }
//        codeTextView.setOnClickListener {
//            val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
//            clipboard?.setPrimaryClip(ClipData.newPlainText("",codeTextView.text))
//            Toast.makeText(requireContext(),"Copied", Toast.LENGTH_SHORT).show()
//
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        shopViewModel.intentTOProductDetails = MutableLiveData()
//    }
//
//
//    private fun bindWomanProductRecyclerView(
//        itemName: List<Product>,
//        intentTOProductDetails: MutableLiveData<Product>
//    ) {
//
//        itemsRecView.adapter= ShopItemAdapter(requireContext(),itemName,intentTOProductDetails)
//
//    }
//


}