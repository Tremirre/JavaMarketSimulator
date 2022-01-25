package simulation.asset;
/**
 * This packages defines, manages and creates assets (in form of asset data) of all types.
 * For clarity, the naming is defined as follows:
 * - asset category - refers to the category of the asset defined in the AssetCategory class.
 * - asset type - refers to an individual asset distinct from any other asset type,
 *   e.g. stock of company A and stock of company B are two distinct asset types, but the 2 stocks of company A are not.
 *   all asset types are held in the asset manager, every asset type being associated with exactly one instance of
 *   asset data. Asset type is commonly identified and referred to through its unique identifying name.
 */