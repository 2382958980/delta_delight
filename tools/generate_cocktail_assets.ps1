Add-Type -AssemblyName System.Drawing

$projectRoot = Split-Path -Parent $PSScriptRoot
$colorsPath = Join-Path $projectRoot "src\main\resources\data\delta_delight\cocktails\ingredient_colors.json"
$templatePath = Join-Path $projectRoot "src\main\resources\assets\delta_delight\textures\item\wines\layer2.png"
$textureOutputDir = Join-Path $projectRoot "src\main\resources\assets\delta_delight\textures\item\wines\generated"
$modelOutputDir = Join-Path $projectRoot "src\main\resources\assets\delta_delight\models\item\cocktails"
$baseModelPath = Join-Path $projectRoot "src\main\resources\assets\delta_delight\models\item\mixed_cocktail.json"
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

New-Item -ItemType Directory -Force -Path $textureOutputDir | Out-Null
New-Item -ItemType Directory -Force -Path $modelOutputDir | Out-Null

function Convert-HexToColor {
    param([string]$Hex)

    $normalized = $Hex.TrimStart('#')
    if ($normalized.Length -ne 6) {
        throw "Invalid color value: $Hex"
    }

    return [System.Drawing.Color]::FromArgb(
        255,
        [Convert]::ToInt32($normalized.Substring(0, 2), 16),
        [Convert]::ToInt32($normalized.Substring(2, 2), 16),
        [Convert]::ToInt32($normalized.Substring(4, 2), 16)
    )
}

function Get-ScaledChannel {
    param([int]$Start, [int]$End, [double]$T, [double]$Shade)

    $value = [Math]::Round(($Start + (($End - $Start) * $T)) * $Shade)
    if ($value -lt 0) {
        return 0
    }
    if ($value -gt 255) {
        return 255
    }
    return [int]$value
}

function Write-Utf8NoBom {
    param([string]$Path, [string]$Content)

    [System.IO.File]::WriteAllText($Path, $Content, $utf8NoBom)
}

$config = Get-Content -Raw $colorsPath | ConvertFrom-Json
$ingredients = @($config.ingredients)
$template = [System.Drawing.Bitmap]::new($templatePath)
$overrides = [System.Collections.Generic.List[object]]::new()
$variantIndex = 1

try {
    for ($i = 0; $i -lt $ingredients.Count; $i++) {
        for ($j = $i; $j -lt $ingredients.Count; $j++) {
            $topIngredient = $ingredients[$i]
            $bottomIngredient = $ingredients[$j]
            $pairKey = "{0}__{1}" -f $topIngredient.key, $bottomIngredient.key
            $outputTexturePath = Join-Path $textureOutputDir ($pairKey + ".png")
            $outputModelPath = Join-Path $modelOutputDir ($pairKey + ".json")

            $topColor = Convert-HexToColor $topIngredient.color
            $bottomColor = Convert-HexToColor $bottomIngredient.color
            $bitmap = [System.Drawing.Bitmap]::new($template.Width, $template.Height)

            try {
                for ($x = 0; $x -lt $template.Width; $x++) {
                    for ($y = 0; $y -lt $template.Height; $y++) {
                        $source = $template.GetPixel($x, $y)
                        if ($source.A -eq 0) {
                            $bitmap.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
                            continue
                        }

                        $gradientT = if ($template.Height -le 1) { 0.5 } else { $y / [double]($template.Height - 1) }
                        $shade = (($source.R + $source.G + $source.B) / 3.0) / 255.0
                        $red = Get-ScaledChannel -Start $topColor.R -End $bottomColor.R -T $gradientT -Shade $shade
                        $green = Get-ScaledChannel -Start $topColor.G -End $bottomColor.G -T $gradientT -Shade $shade
                        $blue = Get-ScaledChannel -Start $topColor.B -End $bottomColor.B -T $gradientT -Shade $shade
                        $bitmap.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($source.A, $red, $green, $blue))
                    }
                }

                $bitmap.Save($outputTexturePath, [System.Drawing.Imaging.ImageFormat]::Png)
            }
            finally {
                $bitmap.Dispose()
            }

            $modelJson = @{
                parent = "minecraft:item/generated"
                textures = @{
                    layer0 = "delta_delight:item/wines/generated/$pairKey"
                    layer1 = "delta_delight:item/wines/layer1"
                }
            } | ConvertTo-Json -Depth 4
            Write-Utf8NoBom -Path $outputModelPath -Content $modelJson

            $overrides.Add(@{
                predicate = @{
                    "delta_delight:cocktail_variant" = [double]$variantIndex
                }
                model = "delta_delight:item/cocktails/$pairKey"
            })

            $variantIndex++
        }
    }

    $baseModel = @{
        parent = "minecraft:item/generated"
        textures = @{
            layer0 = "delta_delight:item/wines/layer2"
            layer1 = "delta_delight:item/wines/layer1"
        }
        overrides = @($overrides.ToArray() | Sort-Object { [double]$_.predicate."delta_delight:cocktail_variant" } -Descending)
    } | ConvertTo-Json -Depth 6
    Write-Utf8NoBom -Path $baseModelPath -Content $baseModel
}
finally {
    $template.Dispose()
}